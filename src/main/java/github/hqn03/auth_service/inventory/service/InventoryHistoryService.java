package github.hqn03.auth_service.inventory.service;

import github.hqn03.auth_service.common.constant.InventoryHistoryType;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import github.hqn03.auth_service.inventory.dto.InventoryHistoryResponse;
import github.hqn03.auth_service.inventory.entity.InventoryHistory;
import github.hqn03.auth_service.inventory.mapper.InventoryMapper;
import github.hqn03.auth_service.inventory.repository.InventoryHistoryRepository;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.sku.entity.Sku;
import github.hqn03.auth_service.sku.repository.SkuRepository;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.repository.UserRepository;
import github.hqn03.auth_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryHistoryService {
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final InventoryMapper inventoryMapper;
    private final UserRepository userRepository;
    private final SkuRepository skuRepository;

    public void create(Long userId,Long skuId, Integer changeQty, InventoryHistoryType type, String referenceId, String note) {
        Sku sku = skuRepository.getReferenceById(skuId);
        User user = userId == null ? null : userRepository.getReferenceById(userId);
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

    public void createBatch(Long userId, Long[] skuIds, int[] deltas, InventoryHistoryType type, String reference, String note) {
        User user = userRepository.getReferenceById(userId);
        List<InventoryHistory> histories =  new ArrayList<>();

        for(int i = 0 ; i < skuIds.length ; i++){
            InventoryHistory a = InventoryHistory.builder()
                    .user(user)
                    .sku(skuRepository.getReferenceById(skuIds[i]))
                    .changeQty(deltas[i])
                    .type(type)
                    .referenceId(reference)
                    .note(note)
                    .build();

            histories.add(a);
        }

        if (!histories.isEmpty()) {
            inventoryHistoryRepository.saveAll(histories);
        }
    }

    public Page<InventoryHistoryResponse> getHistories(Pageable pageable) {
        Page<InventoryHistory> historyPage = inventoryHistoryRepository.findAll(pageable);

        return historyPage.map(inventoryMapper::toInventoryHistoryResponse);
    }


}
