package github.hqn03.auth_service.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.hqn03.auth_service.cart.dto.CartItemDto;
import github.hqn03.auth_service.cart.dto.CartResponse;
import github.hqn03.auth_service.cart.dto.ItemAddRequest;
import github.hqn03.auth_service.cart.dto.QuantityUpdateRequest;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import github.hqn03.auth_service.common.helper.JsonUtils;
import github.hqn03.auth_service.common.service.RedisService;
import github.hqn03.auth_service.product.entity.ProductImage;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final SecurityService securityService;
    private final SkuRepository skuRepository;
    private final RedisService redisService;
    private final JsonUtils jsonUtils;

    private static final String CART_PREFIX = "cart:";
    private static final long TIMEOUT_IN_MINUTES = 10080; // 7 DAYS

    public String getCartKey(String sessionId){
        Long userId = securityService.getUserId();
        return userId != null ?
                CART_PREFIX + "user:" + userId :
                CART_PREFIX + "guest:" + sessionId;
    }

    public Integer addItem(ItemAddRequest request, String sessionId) {
        String key = getCartKey(sessionId);
        String field = request.skuId().toString();

        Object json = redisService.hGet(key, field);
        CartItemDto itemDto;
        if(json != null){
            itemDto = jsonUtils.deserialize(json.toString(), CartItemDto.class);
            itemDto.setQuantity(itemDto.getQuantity() + request.quantity());
        }else{
            Sku sku = skuRepository.getReferenceById(request.skuId());
            String thumbnailUrl = sku.getImages().stream()
                    .filter(ProductImage::getIsMain)
                    .findFirst()
                    .or(() -> sku.getImages().stream().findFirst())
                    .map(ProductImage::getImageUrl).orElse(null);
            itemDto = CartItemDto.builder()
                    .skuId(sku.getId())
                    .name(sku.getProduct().getName())
                    .price(sku.getPrice())
                    .image(thumbnailUrl)
                    .quantity(request.quantity())
                    .build();
        }
        redisService.hSet(key, field, jsonUtils.serialize(itemDto), TIMEOUT_IN_MINUTES);
        return redisService.hSize(key).intValue();
    }

    public CartResponse getCart(String sessionId) {
        String key = getCartKey(sessionId);
        Map<Object, Object> allItems = redisService.hGetAll(key);

        if(allItems.isEmpty()){
            return new CartResponse(Collections.emptyList(), BigDecimal.ZERO, 0);
        }

        List<CartItemDto> items = allItems.values().stream()
                .map(json -> jsonUtils.deserialize(json.toString(), CartItemDto.class))
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, totalPrice, items.size());
    }

    public CartItemDto updateQuantity(QuantityUpdateRequest request, String sessionId) {
        String key = getCartKey(sessionId);
        Object json = redisService.hGet(key, request.skuId().toString());
        if (json == null) {
            throw new ResourceNotFoundException("Item not found in cart");
        }
        CartItemDto itemDto = jsonUtils.deserialize(json.toString(), CartItemDto.class);
        itemDto.setQuantity(request.quantity());

        redisService.hSet(key, request.skuId(), jsonUtils.serialize(itemDto), TIMEOUT_IN_MINUTES);
        return itemDto;
    }

    public Integer deleteItem(Long skuId, String sessionId){
        String key = getCartKey(sessionId);
        String field = skuId.toString();

        redisService.hDelete(key, field);
        int remainingItems = redisService.hSize(key).intValue();
        if(remainingItems == 0){
            redisService.delete(key);
        }else{
            redisService.expire(key, TIMEOUT_IN_MINUTES);
        }
        return remainingItems;
    }

    public void mergeCart(Long customerId, String sessionId){
        String guestKey = CART_PREFIX + "guest:" + sessionId;
        String userKey = CART_PREFIX + "user:" + customerId;

        Map<Object, Object> guestItems = redisService.hGetAll(guestKey);
        if (guestItems == null || guestItems.isEmpty()) return;

        guestItems.forEach((skuIdObj, guestJsonObj) -> {
            String skuId = skuIdObj.toString();
            CartItemDto guestItem = jsonUtils.deserialize(guestJsonObj.toString(), CartItemDto.class);

            Object userJson = redisService.hGet(userKey, skuId);

            if(userJson != null){
                CartItemDto userItem = jsonUtils.deserialize(userJson.toString(), CartItemDto.class);
                userItem.setQuantity(userItem.getQuantity() + guestItem.getQuantity());
                redisService.hSet(userKey, skuId, jsonUtils.serialize(userItem), TIMEOUT_IN_MINUTES);
            }
            else{
                redisService.hSet(userKey, skuId, jsonUtils.serialize(guestItem), TIMEOUT_IN_MINUTES);
            }
        });

        redisService.delete(guestKey);
    }

}
