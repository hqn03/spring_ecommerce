package github.hqn03.auth_service.order.mapper;

import github.hqn03.auth_service.common.helper.JsonUtils;
import github.hqn03.auth_service.order.dto.OrderCreateRequest;
import github.hqn03.auth_service.order.dto.OrderItemDto;
import github.hqn03.auth_service.order.dto.OrderItemResponse;
import github.hqn03.auth_service.order.dto.OrderResponse;
import github.hqn03.auth_service.order.entity.Order;
import github.hqn03.auth_service.order.entity.OrderItem;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "skuCode", source = "skuCode")
    OrderItem toOrderItem(OrderItemDto dto);
}


