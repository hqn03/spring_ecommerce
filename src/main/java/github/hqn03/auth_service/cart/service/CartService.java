package github.hqn03.auth_service.cart.service;

import github.hqn03.auth_service.cart.dto.*;
import github.hqn03.auth_service.cart.entity.Cart;
import github.hqn03.auth_service.cart.entity.CartItem;
import github.hqn03.auth_service.cart.mapper.CartMapper;
import github.hqn03.auth_service.cart.repository.CartItemRepository;
import github.hqn03.auth_service.cart.repository.CartRepository;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import github.hqn03.auth_service.common.service.RedisService;
import github.hqn03.auth_service.customer.entity.Customer;
import github.hqn03.auth_service.customer.repository.CustomerRepository;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private static final String CART_PREFIX = "cart:guest:";
    private static final long TIMEOUT_IN_MINUTES = 10080; // 7 DAYS
    private final RedisService redisService;

    private Cart addItemToDb(Long customerId, ItemAddRequest request) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomerId(customerId);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> cartItem = cart.getItems().stream()
                .filter(i -> i.getSku().getId().equals(request.skuId()))
                .findFirst();

        if (cartItem.isPresent()) {
            CartItem item = cartItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
        } else {
            Sku sku = skuRepository.getReferenceById(request.skuId());
            CartItem item = new CartItem();
            item.setSku(sku);
            item.setQuantity(request.quantity());
            item.setCart(cart);
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public CartSummaryResponse addItem(ItemAddRequest request, String sessionId) {
        Long customerId = securityService.getCustomerId();
        Integer total;
        if (customerId != null) {
            Cart cart = addItemToDb(customerId, request);
            total = cart.getTotalItems();
        } else {
            String key = CART_PREFIX + sessionId;
            redisService.hIncr(key, request.skuId().toString(), request.quantity(), TIMEOUT_IN_MINUTES);

            Map<Object, Object> items = redisService.hGetAll(key);
            total = items.values()
                    .stream()
                    .mapToInt(v -> Integer.parseInt(v.toString()))
                    .sum();
        }

        return new CartSummaryResponse(total, "Add successfully");
    }

    public CartResponse getCartDetail(String sessionId) {
        Long customerId = securityService.getCustomerId();

        if (customerId != null) {
            Cart cart = cartRepository.findByCustomerId(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
            return cartMapper.toCartResponse(cart);
        }

        return getGuestCartDetail(sessionId);
    }

    private CartResponse getGuestCartDetail(String sessionId) {
        String key = CART_PREFIX + sessionId;
        Map<Object, Object> redisItems = redisService.hGetAll(key);

        if (redisItems == null || redisItems.isEmpty()) {
            return new CartResponse(null, Collections.emptyList(), BigDecimal.ZERO, 0);
        }

        List<Long> skuIds = redisItems.keySet()
                .stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();


        List<Sku> skus = skuRepository.findAllByIdIn(skuIds);

        List<ItemResponse> itemResponses = skus.stream().map(sku -> {
            Integer quantity = Integer.valueOf(redisItems.get(sku.getId().toString()).toString());
            return cartMapper.toItemResponse(sku, quantity);
        }).toList();

        BigDecimal totalPrice = itemResponses.stream()
                .map(ItemResponse::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItems = itemResponses.stream()
                .mapToInt(ItemResponse::quantity)
                .sum();

        return new CartResponse(null, itemResponses, totalPrice, totalItems);
    }

    @Transactional
    public ItemResponse updateItemQuantity(QuantityUpdateRequest request, String sessionId) {
        Long customerId = securityService.getCustomerId();

        if (customerId != null) {
            return updateItemInDb(customerId, request);
        }

        String key = CART_PREFIX + sessionId;
        String field = request.skuId().toString();

        redisService.hSet(key, field, request.quantity(), TIMEOUT_IN_MINUTES);

        Sku sku = skuRepository.findById(request.skuId())
                .orElseThrow(() -> new ResourceNotFoundException("Sku not found"));

        return cartMapper.toItemResponse(sku, request.quantity());
    }

    private ItemResponse updateItemInDb(Long customerId, QuantityUpdateRequest request) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems()
                .stream()
                .filter(i -> i.getSku().getId().equals(request.skuId()))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        item.setQuantity(request.quantity());
        cartRepository.save(cart);
        return cartMapper.toItemResponse(item.getSku(), request.quantity());
    }

    @Transactional
    public void deleteItem(Long skuId, String sessionId) {
        Long customerId = securityService.getCustomerId();

        if (customerId != null) {
            deleteItemInDb(customerId, skuId);
        } else {
            String key = CART_PREFIX + sessionId;
            redisService.hDelete(key, skuId.toString());

            redisService.expire(key, TIMEOUT_IN_MINUTES);
        }
    }

    private void deleteItemInDb(Long customerId, Long skuId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getSku().getId().equals(skuId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        cart.removeItem(itemToRemove);

        cartRepository.save(cart);
    }

    @Transactional
    public void mergeCart(Long customerId, String sessionId) {
        String key = CART_PREFIX + sessionId;
        Map<Object, Object> redisItems = redisService.hGetAll(key);

        if (redisItems == null || redisItems.isEmpty()) return;

        Cart cart = cartRepository.findByCustomerId(customerId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomerId(customerId);
            return newCart;
        });
        List<CartItem> dbItems = cart.getItems().stream().toList();

        redisItems.forEach((skuIdObj, qtyObj) -> {
            Long skuId = Long.parseLong(skuIdObj.toString());
            Integer quantity = Integer.parseInt(qtyObj.toString());

            Optional<CartItem> existingItem = dbItems.stream()
                    .filter(item -> item.getSku().getId().equals(skuId))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                Sku sku = skuRepository.getReferenceById(skuId);
                CartItem item = new CartItem();
                item.setSku(sku);
                item.setQuantity(quantity);
                cart.addItem(item);
            }
        });

        cartRepository.save(cart);
        redisService.delete(key);

    }
}
