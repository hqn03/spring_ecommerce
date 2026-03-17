package github.hqn03.auth_service.auth.repository;

import github.hqn03.auth_service.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
