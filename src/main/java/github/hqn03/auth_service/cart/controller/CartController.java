package github.hqn03.auth_service.cart.controller;

import github.hqn03.auth_service.cart.dto.CartItemDto;
import github.hqn03.auth_service.cart.dto.CartResponse;
import github.hqn03.auth_service.cart.dto.ItemAddRequest;
import github.hqn03.auth_service.cart.dto.QuantityUpdateRequest;
import github.hqn03.auth_service.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/me")
    public CartResponse getMyCart(@RequestHeader(value = "X-Session-ID", required = false) String sessionId){
        return cartService.getCart(sessionId);
    }

    @PostMapping
    public ResponseEntity<Integer> addItem(@RequestBody ItemAddRequest request, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        return ResponseEntity.ok(cartService.addItem(request, sessionId));
    }

    @PutMapping
    public ResponseEntity<CartItemDto> updateItemQuantity(@RequestBody QuantityUpdateRequest request, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        return ResponseEntity.ok(cartService.updateQuantity(request, sessionId));
    }

    @DeleteMapping("/sku/{skuId}")
    public ResponseEntity<Integer> deleteItem(@PathVariable Long skuId,  @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        Integer remainingTotal = cartService.deleteItem(skuId, sessionId);
        return ResponseEntity.ok(remainingTotal);
    }

}
