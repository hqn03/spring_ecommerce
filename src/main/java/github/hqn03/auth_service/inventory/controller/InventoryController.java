package github.hqn03.auth_service.inventory.controller;

import github.hqn03.auth_service.inventory.dto.InventoryHistoryResponse;
import github.hqn03.auth_service.inventory.service.InventoryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryHistoryService inventoryHistoryService;

    @GetMapping("/history")
    public ResponseEntity<Page<InventoryHistoryResponse>> getHistories(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable){
        Page<InventoryHistoryResponse> result = inventoryHistoryService.getHistories(pageable);
        return ResponseEntity.ok(result);
    }
}
