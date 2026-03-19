package github.hqn03.auth_service.cart.mapper;

import github.hqn03.auth_service.cart.dto.CartResponse;
import github.hqn03.auth_service.cart.dto.ItemAddRequest;
import github.hqn03.auth_service.cart.dto.ItemResponse;
import github.hqn03.auth_service.cart.entity.Cart;
import github.hqn03.auth_service.cart.entity.CartItem;
import github.hqn03.auth_service.sku.entity.Sku;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "skuId", source = "sku.id")
    @Mapping(target = "productName", source = "sku.product.name")
    @Mapping(target = "productSlug", source = "sku.product.slug")
    @Mapping(target = "colorName", source = "sku.color.name")
    @Mapping(target = "colorCode", source = "sku.color.hexCode")
    @Mapping(target = "sizeName", source = "sku.size.name")
    @Mapping(target = "originalPrice", source = "sku.price")
    @Mapping(target = "discountAmount", source = "sku.discountAmount")
    @Mapping(target = "subTotal", expression = "java(calculateSubTotal(sku, quantity))")
    ItemResponse toItemResponse(Sku sku, Integer quantity);

    default BigDecimal calculateSubTotal(Sku sku, Integer quantity) {
        BigDecimal discount = sku.getDiscountAmount();
        BigDecimal priceUnit = sku.getPrice().subtract(discount);
        return priceUnit.multiply(BigDecimal.valueOf(quantity));
    }

    @Mapping(target = "items", source = "items")
    CartResponse toCartResponse(Cart cart);

    default ItemResponse mapCartItemToResponse(CartItem item){
        return toItemResponse(item.getSku(), item.getQuantity());
    }
}
