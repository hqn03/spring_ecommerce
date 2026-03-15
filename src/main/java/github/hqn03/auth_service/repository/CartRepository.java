package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByCustomerId(Long customerId);

    Optional<Cart> findBySessionId(String sessionId);

    Long id(Long id);
}
