package github.hqn03.auth_service.attribute.repository;

import github.hqn03.auth_service.attribute.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Integer> {
}
