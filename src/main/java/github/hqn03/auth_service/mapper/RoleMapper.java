package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.role.RoleDetailResponse;
import github.hqn03.auth_service.dto.role.RoleRequest;
import github.hqn03.auth_service.dto.role.RoleResponse;
import github.hqn03.auth_service.model.Permission;
import github.hqn03.auth_service.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);

    Role toEntity(RoleRequest roleRequest);

    void updateRoleFromRequest(RoleRequest roleRequest, @MappingTarget Role role);

    @Mapping(target = "permissions", source = "permissions")
    RoleDetailResponse toRoleDetailResponse(Role role);

    default Set<String> toPermissionNames(Set<Permission> permissions) {
        if(permissions==null) return Collections.emptySet();
        return permissions.stream().map(Permission::getName).collect(Collectors.toSet());
    }
}
