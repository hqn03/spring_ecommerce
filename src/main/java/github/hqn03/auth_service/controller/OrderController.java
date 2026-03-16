package github.hqn03.auth_service.controller;

import github.hqn03.auth_service.dto.order.OrderCreateRequest;
import github.hqn03.auth_service.dto.order.OrderResponse;
import github.hqn03.auth_service.model.Order;
import github.hqn03.auth_service.service.OrderService;
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
