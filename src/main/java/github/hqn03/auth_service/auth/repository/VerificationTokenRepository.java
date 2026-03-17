package github.hqn03.auth_service.auth.repository;

import github.hqn03.auth_service.auth.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
