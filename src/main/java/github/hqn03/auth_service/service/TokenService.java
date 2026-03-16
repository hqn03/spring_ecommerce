package github.hqn03.auth_service.service;

import github.hqn03.auth_service.model.User;
import github.hqn03.auth_service.model.VerificationToken;
import github.hqn03.auth_service.repository.UserRepository;
import github.hqn03.auth_service.repository.VerificationTokenRepository;
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
