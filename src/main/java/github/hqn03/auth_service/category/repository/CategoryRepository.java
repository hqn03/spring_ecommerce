package github.hqn03.auth_service.category.repository;

import github.hqn03.auth_service.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
