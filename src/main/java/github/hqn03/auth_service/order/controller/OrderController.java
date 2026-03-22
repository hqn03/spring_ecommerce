package github.hqn03.auth_service.order.controller;

import com.stripe.exception.StripeException;
import github.hqn03.auth_service.order.dto.OrderCreateRequest;
import github.hqn03.auth_service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public String createOrder(@RequestBody OrderCreateRequest request, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) throws StripeException {
        return orderService.placeOrder(request.cartItemIds(), sessionId);
    }
}
