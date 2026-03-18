package github.hqn03.auth_service.order.service;

import github.hqn03.auth_service.cart.entity.CartItem;
import github.hqn03.auth_service.cart.repository.CartItemRepository;
import github.hqn03.auth_service.common.constant.InventoryHistoryType;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.customer.entity.Customer;
import github.hqn03.auth_service.customer.repository.CustomerRepository;
import github.hqn03.auth_service.inventory.service.InventoryHistoryService;
import github.hqn03.auth_service.order.dto.OrderCreateRequest;
import github.hqn03.auth_service.order.dto.OrderResponse;
import github.hqn03.auth_service.order.entity.Order;
import github.hqn03.auth_service.order.entity.OrderItem;
import github.hqn03.auth_service.order.mapper.OrderMapper;
import github.hqn03.auth_service.order.repository.OrderRepository;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.sku.entity.Sku;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final SecurityService securityService;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final InventoryHistoryService inventoryHistoryService;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request){
        Customer customer =  customerRepository.findById(securityService.getCustomerId()).orElse(null);
        List<CartItem> cartItems = cartItemRepository.findAllById(request.cartItemIds());
        Order order = orderMapper.toOrder(request);
        order.setCustomer(customer);
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem ci : cartItems) {
            Sku sku = ci.getSku();

            if( sku.getStockQty() < ci.getQuantity() ){
                throw new AppException("Product is out of stock", HttpStatus.CONFLICT);
            }
            sku.setStockQty(sku.getStockQty() - ci.getQuantity());

            OrderItem item = orderMapper.toOrderItem(sku, ci);

            item.setVariant(String.format("%s, %s", sku.getColor().getName(), sku.getSize().getName())) ;

            totalAmount = totalAmount.add(ci.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            order.addItem(item);
        }

        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);

        for (OrderItem item : order.getItems()) {
            inventoryHistoryService.create(
                    item.getSku().getId(),
                    -item.getQuantity(),
                    InventoryHistoryType.SALE,
                    saved.getOrderCode(),
                    "Khách đặt hàng online"
            );
        }

        cartItemRepository.deleteAll(cartItems);
        return orderMapper.toOrderResponse(saved);
    }
}
