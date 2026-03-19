package github.hqn03.auth_service.sku.repository;

import github.hqn03.auth_service.sku.entity.Sku;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    boolean existsByCode(String code);

    @EntityGraph(attributePaths = {"color", "size", "product", "product.category"})
    List<Sku> findAllByIdIn(Collection<Long> ids);
}
