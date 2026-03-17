package github.hqn03.auth_service.order.mapper;

import github.hqn03.auth_service.cart.entity.CartItem;
import github.hqn03.auth_service.order.dto.OrderCreateRequest;
import github.hqn03.auth_service.order.dto.OrderItemResponse;
import github.hqn03.auth_service.order.dto.OrderResponse;
import github.hqn03.auth_service.order.entity.Order;
import github.hqn03.auth_service.order.entity.OrderItem;
import github.hqn03.auth_service.sku.entity.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "productSlug", source = "sku.product.slug")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    Order toOrder(OrderCreateRequest orderCreateRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originalPrice", source = "sku.price")
    @Mapping(target = "productName", source = "sku.product.name")
    @Mapping(target = "discountAmount", source = "sku.discountAmount")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "quantity", source = "ci.quantity")
    @Mapping(target = "price", source = "ci.price")
    @Mapping(target = "subTotal", source = "ci.subTotal")
    OrderItem toOrderItem(Sku sku, CartItem ci);
}
