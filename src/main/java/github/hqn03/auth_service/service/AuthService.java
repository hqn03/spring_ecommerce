package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.auth.LoginRequest;
import github.hqn03.auth_service.dto.auth.LoginResponse;
import github.hqn03.auth_service.dto.auth.RegisterRequest;
import github.hqn03.auth_service.dto.auth.RegisterResponse;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.mapper.UserMapper;
import github.hqn03.auth_service.model.VerificationToken;
import github.hqn03.auth_service.model.Role;
import github.hqn03.auth_service.model.User;
import github.hqn03.auth_service.repository.VerificationTokenRepository;
import github.hqn03.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final TokenService tokenService;
    private final UserService userService;

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        VerificationToken token = tokenService.generateVerificationToken(user);

        //Call mailservice

        return new RegisterResponse("Registration successful. Please check your email to verify your account.");
    };

    public LoginResponse login(LoginRequest loginRequest) {

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.identifier(),
                    loginRequest.password()
            );

            Authentication authentication = authenticationManager.authenticate(authToken);
            User user = (User) authentication.getPrincipal();
            var token = generateToken(user);

            return new LoginResponse(token);
    }

    private String generateToken(User user) {
        Instant now = Instant.now();

        String scope = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("dev-service")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getUsername())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public void forgotPassword() {};

}
