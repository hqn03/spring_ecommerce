package github.hqn03.auth_service.product.repository;

import github.hqn03.auth_service.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category", "images"})
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category", "skus", "skus.color", "skus.size", "skus.images", "images"})
    Optional<Product> findDetailBySlug(@NonNull String slug);

    Boolean existsBySlug(String slug);

    @EntityGraph(attributePaths = {"category", "skus", "skus.color", "skus.size", "skus.images", "images"})
    Optional<Product> findDetailsById(Long id);
}
