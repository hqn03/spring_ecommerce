package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
