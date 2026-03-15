package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.cart.CartResponse;
import github.hqn03.auth_service.dto.cart.ItemAddRequest;
import github.hqn03.auth_service.dto.cart.ItemResponse;
import github.hqn03.auth_service.model.Cart;
import github.hqn03.auth_service.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "sku.id", source = "request.skuId")
    CartItem toCartItem(Cart cart, ItemAddRequest request);

    @Mapping(target = "skuId", source = "sku.id")
    @Mapping(target = "productName", source = "sku.product.name")
    @Mapping(target = "productSlug", source = "sku.product.slug")
    @Mapping(target = "colorName", source = "sku.color.name")
    @Mapping(target = "colorCode", source = "sku.color.hexCode")
    @Mapping(target = "sizeName", source = "sku.size.name")
    @Mapping(target = "price", source = "sku.price")
    ItemResponse toItemResponse(CartItem item);


    CartResponse toCartResponse(Cart cart);
}
