package github.hqn03.auth_service.auth.service;

import github.hqn03.auth_service.auth.dto.auth.LoginRequest;
import github.hqn03.auth_service.auth.dto.auth.LoginResponse;
import github.hqn03.auth_service.auth.dto.auth.RegisterRequest;
import github.hqn03.auth_service.auth.dto.auth.RegisterResponse;
import github.hqn03.auth_service.auth.entity.VerificationToken;
import github.hqn03.auth_service.cart.service.CartService;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.service.RedisService;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final CartService cartService;
    private final RedisService redisService;

    @Transactional
    public String register(RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        String token = UUID.randomUUID().toString();
        redisService.set("verify_email:" + token, user.getId(), 15);

        log.info(token);
        //Call mail service
        return "Registration successful. Please check your email to verify your account.";
    }

    @Transactional
    public String login(LoginRequest loginRequest, String sessionId) {
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

        return generateToken(user);
    }

    @Transactional
    public String verifyToken(String token) {
        String key = "verify_email:" + token;
        Object value = redisService.get(key);

        if(value == null) throw new AppException("Token is invalid", HttpStatus.NOT_FOUND);

        Long id = Long.valueOf(value.toString());

        userService.enableUser(id);
        redisService.delete(key);

        return "Account verified successfully!";
    }

    private String generateToken(User user) {
        Instant now = Instant.now();

        String scope = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));


        var claimsBuilder = JwtClaimsSet.builder()
                .issuer("dev-service")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getUsername())
                .claim("scope", scope);

        if (user.getCustomer() != null) {
            claimsBuilder.claim("customer", user.getCustomer().getId());
        }

        JwtClaimsSet claims = claimsBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public void forgotPassword() {
    }

    ;

}
