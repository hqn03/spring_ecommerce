package github.hqn03.auth_service.security;

public record JwtPrincipal(
        Long customerId,
        String username
) {
}
