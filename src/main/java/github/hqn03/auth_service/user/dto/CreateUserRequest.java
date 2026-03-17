package github.hqn03.auth_service.user.dto;

import java.util.Set;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        Set<Integer> roleIds) {
}
