package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.auth.LoginRequest;
import github.hqn03.auth_service.dto.auth.LoginResponse;
import github.hqn03.auth_service.dto.auth.RegisterRequest;
import github.hqn03.auth_service.dto.auth.RegisterResponse;
import github.hqn03.auth_service.model.User;
import github.hqn03.auth_service.model.VerificationToken;
import github.hqn03.auth_service.repository.VerificationTokenRepository;
import github.hqn03.auth_service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    private final CartService cartService;

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        VerificationToken token = tokenService.generateVerificationToken(user.getId());

        return new RegisterResponse("Registration successful. Please check your email to verify your account.");
    }

    ;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest, String sessionId) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.identifier(),
                loginRequest.password()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);
        User user = (User) authentication.getPrincipal();

        boolean isCustomer = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

        if(isCustomer){
            cartService.mergeCart(user.getCustomer().getId(), sessionId);
        }

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
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getUsername())
                .claim("scope", scope)
                .claim("customer", user.getCustomer().getId())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public void forgotPassword() {
    }

    ;

}
