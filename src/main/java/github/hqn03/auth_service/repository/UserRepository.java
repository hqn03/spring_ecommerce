package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameOrEmailAndIdNot(String username, String email, Long id);
    boolean existsByUsernameOrEmail(String username, String email);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findByUsernameOrEmail(String username, String email);


    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findWithRolePermissionById(Long id);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findWithRoleById(Long id);

    @Override
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "roles")
    @NonNull
    Optional<User> findById(@NonNull Long id);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
