package github.hqn03.auth_service.cart.mapper;

import github.hqn03.auth_service.cart.dto.CartResponse;
import github.hqn03.auth_service.cart.dto.ItemAddRequest;
import github.hqn03.auth_service.cart.dto.ItemResponse;
import github.hqn03.auth_service.cart.entity.Cart;
import github.hqn03.auth_service.cart.entity.CartItem;
import github.hqn03.auth_service.sku.entity.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "id", ignore = true)
    CartItem toCartItem(Cart cart, Sku sku, ItemAddRequest request);

    @Mapping(target = "skuId", source = "sku.id")
    @Mapping(target = "productName", source = "sku.product.name")
    @Mapping(target = "productSlug", source = "sku.product.slug")
    @Mapping(target = "colorName", source = "sku.color.name")
    @Mapping(target = "colorCode", source = "sku.color.hexCode")
    @Mapping(target = "sizeName", source = "sku.size.name")
    @Mapping(target = "originalPrice", source = "sku.price")
    @Mapping(target = "discountAmount", source = "sku.discountAmount")
    ItemResponse toItemResponse(CartItem item);


    CartResponse toCartResponse(Cart cart);
}
