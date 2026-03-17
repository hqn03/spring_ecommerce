package github.hqn03.auth_service.user.mapper;

import github.hqn03.auth_service.auth.dto.auth.RegisterRequest;
import github.hqn03.auth_service.auth.entity.Role;
import github.hqn03.auth_service.user.dto.CreateUserRequest;
import github.hqn03.auth_service.user.dto.UpdateUserRequest;
import github.hqn03.auth_service.user.dto.UserResponse;
import github.hqn03.auth_service.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(target = "password", ignore = true)
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "toRoleNames")
    UserResponse toUserResponse(User user);

    @Named("toRoleNames")
    default Set<String> toRoleNames(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
