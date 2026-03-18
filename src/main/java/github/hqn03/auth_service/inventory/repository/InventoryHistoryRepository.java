package github.hqn03.auth_service.inventory.repository;

import github.hqn03.auth_service.inventory.entity.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Long> {
}
