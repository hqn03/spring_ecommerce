package github.hqn03.auth_service.inventory.mapper;

import github.hqn03.auth_service.inventory.dto.InventoryHistoryResponse;
import github.hqn03.auth_service.inventory.entity.InventoryHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "type", source = "type")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "skuCode", source = "sku.code")
    InventoryHistoryResponse toInventoryHistoryResponse(InventoryHistory inventoryHistory);
}
