package github.hqn03.auth_service.user.controller;

import github.hqn03.auth_service.user.dto.CreateUserRequest;
import github.hqn03.auth_service.user.dto.UpdateUserRequest;
import github.hqn03.auth_service.user.dto.UserResponse;
import github.hqn03.auth_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RestClient.Builder builder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER:CREATE')")
    public UserResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER:READ')")
    public Page<UserResponse> getUsers(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER:READ')")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER:UPDATE')")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.updateUser(id, updateUserRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER:DELETE')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "Deleted successfully";
    }
}
