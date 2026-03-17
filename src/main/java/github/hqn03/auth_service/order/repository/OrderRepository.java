package github.hqn03.auth_service.order.repository;

import github.hqn03.auth_service.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
