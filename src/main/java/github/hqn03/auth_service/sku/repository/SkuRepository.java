package github.hqn03.auth_service.sku.repository;

import github.hqn03.auth_service.sku.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    boolean existsBySkuCode(String skuCode);
}
