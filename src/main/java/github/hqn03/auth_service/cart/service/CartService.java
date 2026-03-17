package github.hqn03.auth_service.cart.service;

import github.hqn03.auth_service.cart.dto.CartResponse;
import github.hqn03.auth_service.cart.dto.ItemAddRequest;
import github.hqn03.auth_service.cart.dto.ItemResponse;
import github.hqn03.auth_service.cart.dto.QuantityUpdateRequest;
import github.hqn03.auth_service.cart.entity.Cart;
import github.hqn03.auth_service.cart.entity.CartItem;
import github.hqn03.auth_service.cart.mapper.CartMapper;
import github.hqn03.auth_service.cart.repository.CartItemRepository;
import github.hqn03.auth_service.cart.repository.CartRepository;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import github.hqn03.auth_service.customer.entity.Customer;
import github.hqn03.auth_service.customer.repository.CustomerRepository;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final SecurityService securityService;
    private final CustomerRepository customerRepository;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;
    private final SkuRepository skuRepository;

    private void validateCartItemOwnership(CartItem item, String sessionId) {
        Long customerId = securityService.getCustomerId();

        if (item.getCart().getCustomer() == null) {
            // save by session id
            if (!item.getCart().getSessionId().equals(sessionId)) {
                throw new AppException("Access denied", HttpStatus.FORBIDDEN);
            }
        } else {
            // save by customer
            if(!item.getCart().getCustomer().getId().equals(customerId)){
                throw new AppException("You don't own this item", HttpStatus.FORBIDDEN);
            }
        }
    }

    public Cart createCart(Long customerId, String sessionId) {
        Customer customer = null;
        if (customerId != null) {
            customer = customerRepository.getReferenceById(customerId);
        }
        return new Cart(customer, sessionId);
    }

    private Cart getCart(Long customerId, String sessionId) {
        if (customerId != null) {
            return cartRepository.findByCustomerId(customerId).orElse(null);
        }

        if (sessionId == null) {
            throw new ResourceNotFoundException("Session id is null");
        }

        return cartRepository.findBySessionId(sessionId).orElse(null);
    }

    @Transactional
    public CartResponse addItem(ItemAddRequest request, String sessionId) {
        Long customerId = securityService.getCustomerId();
        Cart cart = getCart(customerId, sessionId);

        if(cart == null) {
            cart = createCart(customerId, sessionId);
        }

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getSku().getId().equals(request.skuId()))
                .findFirst().orElse(null);

        int currentQuantityInCart = (item != null) ? item.getQuantity() : 0;
        int newTotalQuantity = currentQuantityInCart + request.quantity();

        Sku sku = skuRepository.getReferenceById(request.skuId());
        if (newTotalQuantity > sku.getStockQty()) {
            throw new AppException("Stock is not available", HttpStatus.BAD_REQUEST);
        }

        if (item != null) {
            item.setQuantity(newTotalQuantity);
        }
        else {
            CartItem newItem = cartMapper.toCartItem(cart, sku, request);
            cart.addItem(newItem);
        }

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    public CartResponse getCartDetail(String sessionId) {
        Long customerId = securityService.getCustomerId();
        Cart cart = getCart(customerId, sessionId);

        return cartMapper.toCartResponse((cart == null ? new Cart() : cart));
    }

    @Transactional
    public ItemResponse updateItemQuantity(QuantityUpdateRequest request, String sessionId) {
        CartItem item = cartItemRepository.findById(request.cartItemId())
                .orElseThrow(() -> new ResourceNotFoundException("item not found"));

        validateCartItemOwnership(item, sessionId);

        if(item.getSku().getStockQty() < request.quantity()){
            throw new AppException("Invalid quantity", HttpStatus.BAD_REQUEST);
        }

        item.setQuantity(request.quantity());
        return cartMapper.toItemResponse(item);
    }

    @Transactional
    public void deleteItem(Long cartItemId, String sessionId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("item not found"));

        validateCartItemOwnership(item, sessionId);

        cartItemRepository.delete(item);
    }

    @Transactional
    public void  mergeCart(Long customerId, String sessionId) {
        Cart anonymousCart = cartRepository.findBySessionId(sessionId).orElse(null);
        if(anonymousCart == null || anonymousCart.getItems().isEmpty()) return;

        Cart userCart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> createCart(customerId, null));
        userCart.setSessionId(sessionId);

        for(CartItem anonymousItem : anonymousCart.getItems()) {
            CartItem existingItem = userCart.getItems().stream()
                    .filter(i -> i.getSku().getId().equals(anonymousItem.getSku().getId()))
                    .findFirst()
                    .orElse(null);

            if(existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + anonymousItem.getQuantity());
            }else {
                userCart.addItem(anonymousItem);
            }
        }

        cartRepository.save(userCart);
    }
}
