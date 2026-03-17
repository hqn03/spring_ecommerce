package github.hqn03.auth_service.order.controller;

import github.hqn03.auth_service.order.dto.OrderCreateRequest;
import github.hqn03.auth_service.order.dto.OrderResponse;
import github.hqn03.auth_service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request){
        return orderService.createOrder(request);
    }
}
