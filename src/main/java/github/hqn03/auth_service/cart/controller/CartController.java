package github.hqn03.auth_service.cart.controller;

import github.hqn03.auth_service.cart.dto.*;
import github.hqn03.auth_service.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/me")
    public CartResponse getMyCart(@RequestHeader(value = "X-Session-ID", required = false) String sessionId){
        return cartService.getCartDetail(sessionId);
    }

    @PostMapping
    public CartSummaryResponse addItem(@RequestBody ItemAddRequest request, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        return cartService.addItem(request, sessionId);
    }

    @PutMapping
    public ItemResponse updateItemQuantity(@RequestBody QuantityUpdateRequest request, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        return cartService.updateItemQuantity(request, sessionId);
    }

    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable Long id,  @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        cartService.deleteItem(id, sessionId);
        return "Success";
    }

}
