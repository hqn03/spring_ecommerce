package github.hqn03.auth_service.order.service;

import com.stripe.exception.StripeException;
import github.hqn03.auth_service.cart.dto.CartItemDto;
import github.hqn03.auth_service.cart.service.CartService;
import github.hqn03.auth_service.common.constant.InventoryHistoryType;
import github.hqn03.auth_service.common.constant.OrderStatus;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import github.hqn03.auth_service.common.helper.JsonUtils;
import github.hqn03.auth_service.common.service.RedisService;
import github.hqn03.auth_service.inventory.service.InventoryHistoryService;
import github.hqn03.auth_service.order.dto.OrderItemDto;
import github.hqn03.auth_service.order.entity.Order;
import github.hqn03.auth_service.order.entity.OrderItem;
import github.hqn03.auth_service.order.mapper.OrderMapper;
import github.hqn03.auth_service.order.repository.OrderRepository;
import github.hqn03.auth_service.payment.constant.PaymentStatus;
import github.hqn03.auth_service.payment.repository.PaymentRepository;
import github.hqn03.auth_service.payment.service.PaymentService;
import github.hqn03.auth_service.payment.service.StripeService;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import github.hqn03.auth_service.sku.service.SkuService;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CartService cartService;
    private final RedisService redisService;
    private final JsonUtils jsonUtils;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final SkuService skuService;
    private final StripeService stripeService;
    private final UserRepository userRepository;

    private static final String ORDER_PREFIX = "order:";
    private final PaymentService paymentService;
    private final InventoryHistoryService inventoryHistoryService;
    private final PaymentRepository paymentRepository;
    private final SkuRepository skuRepository;


    public String placeOrder(List<Long> skuIds, String sessionId) throws StripeException {
        String cartKey = cartService.getCartKey(sessionId);
        Map<Object, Object> cartItems = redisService.hGetAll(cartKey);

        String tempId = UUID.randomUUID().toString();
        String orderKey = ORDER_PREFIX + tempId;

        List<Sku> skus = skuRepository.findAllByIdIn(skuIds);
        if (skus.size() != skuIds.size()) {
            throw new ResourceNotFoundException("Một số sản phẩm không còn tồn tại.");
        }

        Map<Long, Sku> skuMap = skus.stream().collect(Collectors.toMap(Sku::getId, Function.identity()));

        Map<Object, Object> finalOrderItems = new HashMap<>();
        for(Long skuId : skuIds){
            Object jsonObj = cartItems.get(skuId.toString());
            if (jsonObj == null) continue;

            CartItemDto item = jsonUtils.deserialize(jsonObj.toString(), CartItemDto.class);
            Sku sku = skuMap.get(skuId);

            if(sku == null) throw new ResourceNotFoundException("sku not found");
            OrderItemDto orderItem = OrderItemDto.builder()
                    .skuCode(sku.getCode())
                    .name(item.getName())
                    .price(sku.getPrice())
                    .image(item.getImage())
                    .quantity(item.getQuantity())
                    .variant(sku.getSize().getName() + ", " + sku.getColor().getName())
                    .build();

            finalOrderItems.put(skuId.toString(), jsonUtils.serialize(orderItem));
        }
        redisService.hSetAll(orderKey, finalOrderItems, 15);
        return stripeService.createCheckoutSession(orderKey, cartKey);
    }


    @Transactional
    public void createOrderAfterPayment(String sessionId, String orderKey, String cartKey, Long userId, String fullName, String address, String phone, String currency, String method){
        if(paymentRepository.existsBySessionId(sessionId)){
            return;
        }

        Map<Object, Object> items = redisService.hGetAll(orderKey);
        if(items.isEmpty()){
            throw new ResourceNotFoundException("Giỏ hàng đã hết hạn hoặc không tồn tại.");
        }

        User user = userRepository.getReferenceById(userId);
        Order order = Order.builder()
                .user(user)
                .shippingAddress(address)
                .shippingFullName(fullName)
                .shippingPhone(phone)
                .status(OrderStatus.PROCESSING)
                .build();


        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        int size = items.size();
        Long[] skuIds = new Long[size];
        int[] deltas = new int[size];

        int i = 0;
        for (Map.Entry<Object, Object> entry : items.entrySet()) {

            Long skuId = Long.parseLong(entry.getKey().toString());
            skuIds[i] =  skuId;
            OrderItemDto dto = jsonUtils.deserialize(entry.getValue().toString(), OrderItemDto.class);

            deltas[i] = -dto.getQuantity();

            //Tru kho
            // A. Trừ kho nguyên tử (Bắt buộc theo ID để đảm bảo logic s.stockQty + :delta >= 0)
            int rows = skuRepository.updateStock(skuId, -dto.getQuantity());
            if (rows == 0) throw new AppException("Sản phẩm " + dto.getName() + " không đủ tồn kho", HttpStatus.BAD_REQUEST);

            OrderItem orderItem = orderMapper.toOrderItem(dto);
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            i++;
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        paymentService.createPayment(sessionId, PaymentStatus.PAID, totalAmount, currency, savedOrder.getId(), method);

        inventoryHistoryService.createBatch(userId, skuIds,deltas,InventoryHistoryType.SALE,savedOrder.getOrderCode(),"buying");

        //CLEAN_UP
        redisService.delete(orderKey);
        if(cartKey != null &&  !cartKey.isEmpty()) {
            Object[] skuIdsToRemove = items.keySet().toArray();
            redisService.hDelete(cartKey, skuIdsToRemove);
        }
        items.forEach((skuIdObj, jsonObj) -> {
            redisService.hDelete(cartKey, skuIdObj.toString());
        });
    }
}
