package github.hqn03.auth_service.auth.repository;

import github.hqn03.auth_service.auth.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    @EntityGraph(attributePaths = {"permissions"})
    Optional<Role> findWithPermissionById(Integer id);

}
