package github.hqn03.auth_service.payment.repository;

import github.hqn03.auth_service.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsBySessionId(String sessionId);
}
