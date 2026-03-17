package github.hqn03.auth_service.user.dto;

import java.util.Set;

public record UserDetailResponse(
        Long id,
        String username,
        String email,
        Set<String> roles,
        Set<String> permissions
) {
}
