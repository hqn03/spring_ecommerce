package github.hqn03.auth_service.auth.dto.role;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record RoleRequest(
        @NotBlank String name,
        String description,
        Set<Integer> permissionIds
) {
}
