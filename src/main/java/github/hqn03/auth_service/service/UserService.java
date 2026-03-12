package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.auth.RegisterRequest;
import github.hqn03.auth_service.dto.user.CreateUserRequest;
import github.hqn03.auth_service.dto.user.UpdateUserRequest;
import github.hqn03.auth_service.dto.user.UserDetailResponse;
import github.hqn03.auth_service.dto.user.UserResponse;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.UserMapper;
import github.hqn03.auth_service.model.Permission;
import github.hqn03.auth_service.model.Role;
import github.hqn03.auth_service.model.User;
import github.hqn03.auth_service.repository.PermissionRepository;
import github.hqn03.auth_service.repository.RoleRepository;
import github.hqn03.auth_service.repository.UserRepository;
import github.hqn03.auth_service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;
    private final UserMapper userMapper;
    private final RoleService roleService;

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
    }

    @Transactional
    public User registerUser(RegisterRequest request){
        validateUniqueFields(null, request.username(), request.password());

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleService.getUserRole());

        return userRepository.save(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        this.validateUniqueFields(null, createUserRequest.username(), createUserRequest.email());

        String adminUsername = securityService.getUsername();
        Set<Role> roles = roleService.findAllByIds(createUserRequest.roleIds());
        this.validateRoleAssignment(roles);

        User user = userMapper.toEntity(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        user.setRoles(roles);

        User saved = userRepository.save(user);

        Set<String> rolesName = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        log.info("Admin '{}' created user '{}' with email '{}' and roles {}", adminUsername, saved.getUsername(), saved.getEmail(), rolesName);

        return userMapper.toUserResponse(saved);
    }

    public Page<UserResponse> getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toUserResponse);
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findWithRoleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {
        String adminUsername = securityService.getUsername();
        User user = findById(id);

        this.validateUniqueFields(id, updateUserRequest.username(), updateUserRequest.email());

        userMapper.updateUserFromRequest(updateUserRequest, user);

        if (updateUserRequest.roleIds() != null && !updateUserRequest.roleIds().isEmpty()) {
            Set<Role> roles = roleService.findAllByIds(updateUserRequest.roleIds());
            validateRoleAssignment(roles);
            user.setRoles(roles);
        }

        log.info("Admin '{}' updated user '{}' with email '{}'",
                adminUsername, updateUserRequest.username(), updateUserRequest.email());

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void deleteUser(Long id  ){
        User user = this.findById(id);
        userRepository.delete(user);
    }

    private void validateRoleAssignment(Set<Role> targetRoles){
        if (securityService.isSuperAdmin()) return;
        boolean hasHighLevelRole = targetRoles.stream()
                .anyMatch(r -> r.getName().equals("ADMIN") || r.getName().equals("SUPER_ADMIN"));
        if(hasHighLevelRole) {
            throw new AccessDeniedException("You are not allowed to assign role Admin and Super Admin");

        };
    }

    private void validateUniqueFields(Long currentUserId, String username, String email){
        userRepository.findByUsername(username).ifPresent(existingUser -> {
            if(!Objects.equals(currentUserId, existingUser.getId())){
                throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
            }
        });

        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if(!Objects.equals(currentUserId, existingUser.getId())){
                throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
            }
        });
    }
}
