package github.hqn03.auth_service.sku.repository;

import github.hqn03.auth_service.sku.entity.Sku;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;

import java.util.Collection;
import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    boolean existsByCode(String code);

    @EntityGraph(attributePaths = {"size", "color"})
    List<Sku> findAllByIdIn(Collection<Long> ids);

    @Modifying
    @Query("UPDATE Sku s SET s.stockQty = s.stockQty + :delta " +
            "WHERE s.id = :id AND (s.stockQty + :delta) >= 0")
    int updateStock(@Param("id") Long id, @Param("delta") Integer delta);
}
