package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = {"category"})
    @NonNull
    List<Product> findAll();

    @EntityGraph(attributePaths = {"category", "skus", "skus.color", "skus.size"})
    Optional<Product> findDetailBySlug(@NonNull String slug);

    Boolean existsBySlug(String slug);

    @EntityGraph(attributePaths = {"category", "skus", "skus.color", "skus.size"})
    Optional<Product> findDetailsById(Long id);
}
