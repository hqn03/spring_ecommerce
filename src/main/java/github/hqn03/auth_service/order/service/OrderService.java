package github.hqn03.auth_service.order.service;

import github.hqn03.auth_service.cart.entity.CartItem;
import github.hqn03.auth_service.cart.repository.CartItemRepository;
import github.hqn03.auth_service.customer.entity.Customer;
import github.hqn03.auth_service.customer.repository.CustomerRepository;
import github.hqn03.auth_service.order.dto.OrderCreateRequest;
import github.hqn03.auth_service.order.dto.OrderResponse;
import github.hqn03.auth_service.order.entity.Order;
import github.hqn03.auth_service.order.entity.OrderItem;
import github.hqn03.auth_service.order.mapper.OrderMapper;
import github.hqn03.auth_service.order.repository.OrderRepository;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.sku.entity.Sku;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request){
        Customer customer =  customerRepository.findById(securityService.getCustomerId()).orElse(null);
        List<CartItem> cartItems = cartItemRepository.findAllById(request.cartItemIds());
        Order order = orderMapper.toOrder(request);
        order.setCustomer(customer);
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem ci : cartItems) {
            Sku sku = ci.getSku();
            OrderItem item = orderMapper.toOrderItem(sku, ci);


            String color = ci.getSku().getColor().getName();
            String size = ci.getSku().getSize().getName();
            item.setVariant(String.format("%s, %s",color, size));

            totalAmount = totalAmount.add(ci.getSku().getPrice());
            order.addItem(item);
        }

        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);
        cartItemRepository.deleteAllById(request.cartItemIds());
        return orderMapper.toOrderResponse(saved);
    }
}
