package github.hqn03.auth_service.inventory.service;

import github.hqn03.auth_service.common.constant.InventoryHistoryType;
import github.hqn03.auth_service.inventory.dto.InventoryHistoryResponse;
import github.hqn03.auth_service.inventory.entity.InventoryHistory;
import github.hqn03.auth_service.inventory.mapper.InventoryMapper;
import github.hqn03.auth_service.inventory.repository.InventoryHistoryRepository;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryHistoryService {
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final SkuRepository skuRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final InventoryMapper inventoryMapper;

    public void create(Long skuId, Integer changeQty, InventoryHistoryType type, String referenceId, String note) {
        Long userId = securityService.getUserId();
        Sku sku = skuRepository.getReferenceById(skuId);
        User user = userRepository.getReferenceById(userId);
        InventoryHistory inventoryHistory = InventoryHistory.builder()
                .sku(sku)
                .user(user)
                .changeQty(changeQty)
                .type(type)
                .referenceId(referenceId)
                .note(note)
                .build();

        inventoryHistoryRepository.save(inventoryHistory);
    }

    public Page<InventoryHistoryResponse> getHistories(Pageable pageable) {
        Page<InventoryHistory> historyPage = inventoryHistoryRepository.findAll(pageable);

        return historyPage.map(inventoryMapper::toInventoryHistoryResponse);
    }


}
