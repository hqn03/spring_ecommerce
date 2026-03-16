package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    boolean existsBySkuCode(String skuCode);
}
