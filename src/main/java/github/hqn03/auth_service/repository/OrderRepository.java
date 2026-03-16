package github.hqn03.auth_service.repository;

import github.hqn03.auth_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
