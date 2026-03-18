package github.hqn03.auth_service.security;

public record JwtPrincipal(
        String userId,
        String customerId
) {
}
