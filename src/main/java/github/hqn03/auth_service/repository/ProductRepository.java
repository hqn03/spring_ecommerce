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

    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findBySlug(@NonNull String slug);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findById(@NonNull Long id);

    Boolean existsBySlug(String slug);
}
