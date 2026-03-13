package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Integer> {
}
