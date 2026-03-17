package github.hqn03.auth_service.user.dto;

import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        Set<String> roles) {
}
