package github.hqn03.auth_service.user.dto;

import java.util.Set;

public record UpdateUserRequest(
        String username,
        String email,
        Set<Integer> roleIds) {
}
