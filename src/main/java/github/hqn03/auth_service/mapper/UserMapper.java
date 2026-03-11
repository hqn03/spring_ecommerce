package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.auth.RegisterRequest;
import github.hqn03.auth_service.dto.user.CreateUserRequest;
import github.hqn03.auth_service.dto.user.UpdateUserRequest;
import github.hqn03.auth_service.dto.user.UserDetailResponse;
import github.hqn03.auth_service.dto.user.UserResponse;
import github.hqn03.auth_service.model.Permission;
import github.hqn03.auth_service.model.Role;
import github.hqn03.auth_service.model.User;
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
    User toEntity(RegisterRequest  request);

    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(User user);

    default Set<String> mapRolesToNames(Set<Role> roles){
        if(roles==null) return null;
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
