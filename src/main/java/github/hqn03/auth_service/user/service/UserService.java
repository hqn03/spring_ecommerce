package github.hqn03.auth_service.user.service;

import github.hqn03.auth_service.auth.dto.auth.RegisterRequest;
import github.hqn03.auth_service.auth.entity.Role;
import github.hqn03.auth_service.auth.service.RoleService;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import github.hqn03.auth_service.customer.service.CustomerService;
import github.hqn03.auth_service.security.SecurityService;
import github.hqn03.auth_service.user.dto.CreateUserRequest;
import github.hqn03.auth_service.user.dto.UpdateUserRequest;
import github.hqn03.auth_service.user.dto.UserResponse;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.mapper.UserMapper;
import github.hqn03.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
    private final CustomerService customerService;

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
    }

    private void validateRoleAssignment(Set<Role> targetRoles) {
        if (securityService.isSuperAdmin()) return;
        boolean hasHighLevelRole = targetRoles.stream()
                .anyMatch(r -> r.getName().equals("ADMIN") || r.getName().equals("SUPER_ADMIN"));
        if (hasHighLevelRole) {
            throw new AccessDeniedException("You are not allowed to assign role Admin and Super Admin");

        }
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if(userRepository.existsByUsernameOrEmail(request.username(), request.password())){
            throw new AppException("Username or email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.addRole(roleService.getRoleUser());

        User saved = userRepository.save(user);
        customerService.createCustomer(saved.getId(), request);
        return saved;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByUsernameOrEmail(request.username(), request.email())){
            throw new AppException("Username or email already exists", HttpStatus.BAD_REQUEST);
        };

        String adminUsername = securityService.getUsername();
        Set<Role> roles;
        if(request.roleIds() == null || request.roleIds().isEmpty()){
            roles = new HashSet<>();
        }else {
            roles = roleService.findAllByIds(request.roleIds());
            this.validateRoleAssignment(roles);
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
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
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findById(id);

        if (userRepository.existsByUsernameAndIdNot(request.username(), id)) {
            throw new AppException("Username already taken", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new AppException("Email already registered", HttpStatus.BAD_REQUEST);
        }

        userMapper.updateUserFromRequest(request, user);

        if (request.roleIds() != null && !request.roleIds().isEmpty()) {
            Set<Role> roles = roleService.findAllByIds(request.roleIds());
            validateRoleAssignment(roles);
            user.setRoles(roles);
        }

        String adminUsername = securityService.getUsername();
        log.info("Admin '{}' updated user ID {}", adminUsername, id);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        userRepository.delete(user);
    }

    public void enableUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEnabled(true);
    }
}
