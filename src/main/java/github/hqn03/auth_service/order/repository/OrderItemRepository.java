package github.hqn03.auth_service.order.repository;

import github.hqn03.auth_service.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
