package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.cart.CartResponse;
import github.hqn03.auth_service.dto.cart.ItemAddRequest;
import github.hqn03.auth_service.dto.cart.ItemResponse;
import github.hqn03.auth_service.dto.cart.QuantityUpdateRequest;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.CartMapper;
import github.hqn03.auth_service.model.Cart;
import github.hqn03.auth_service.model.CartItem;
import github.hqn03.auth_service.model.Customer;
import github.hqn03.auth_service.model.Sku;
import github.hqn03.auth_service.repository.CartItemRepository;
import github.hqn03.auth_service.repository.CartRepository;
import github.hqn03.auth_service.repository.CustomerRepository;
import github.hqn03.auth_service.repository.SkuRepository;
import github.hqn03.auth_service.security.SecurityService;
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

    private Cart createCart(Long  customerId, String sessionId) {
        Customer customer = null;
        if (customerId != null) {
            customer = customerRepository.getReferenceById(customerId);
        }
        return cartRepository.saveAndFlush(new Cart(customer, sessionId));
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
    public ItemResponse addItem(ItemAddRequest request, String sessionId) {
        Long customerId = securityService.getCustomerId();
        Cart cart = getCart(customerId, sessionId);

        if(cart == null) {
            cart = createCart(customerId, sessionId);
        }

        final Cart finalCart = cart;
        CartItem item = cartItemRepository.findByCartAndSkuId(cart, request.skuId())
                .orElseGet(() -> cartMapper.toCartItem(finalCart, request));

        item.setQuantity(item.getQuantity() + request.quantity());

        return cartMapper.toItemResponse(cartItemRepository.save(item));
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
}
