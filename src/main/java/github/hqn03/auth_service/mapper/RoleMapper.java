package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.role.*;
import github.hqn03.auth_service.model.Permission;
import github.hqn03.auth_service.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

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
        return permissions.stream().map(Permission::getName).collect(Collectors.toSet());
    }
}
