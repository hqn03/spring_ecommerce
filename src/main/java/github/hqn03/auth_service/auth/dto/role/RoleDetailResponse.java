package github.hqn03.auth_service.auth.dto.role;

import java.util.Set;

public record RoleDetailResponse(Long id, String name, String description, Set<String> permissions) {
}
