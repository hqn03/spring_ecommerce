package github.hqn03.auth_service.auth.service;

import github.hqn03.auth_service.auth.entity.VerificationToken;
import github.hqn03.auth_service.auth.repository.VerificationTokenRepository;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public VerificationToken generateVerificationToken(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiredAt(LocalDateTime.now().plusDays(1));
        return verificationTokenRepository.save(verificationToken);
    }
}
