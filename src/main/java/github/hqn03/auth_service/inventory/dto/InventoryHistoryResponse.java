package github.hqn03.auth_service.inventory.dto;

public record InventoryHistoryResponse(
        Long id,
        Long username,
        Long skuCode,
        String type,
        String referenceId,
        Integer changeQty,
        String note
) {

}
