package github.hqn03.auth_service.auth.controller;

import github.hqn03.auth_service.auth.dto.role.RoleDetailResponse;
import github.hqn03.auth_service.auth.dto.role.RoleRequest;
import github.hqn03.auth_service.auth.dto.role.RoleResponse;
import github.hqn03.auth_service.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleResponse> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDetailResponse getRole(@PathVariable Integer id) {
        return roleService.getRole(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDetailResponse createRole(@RequestBody RoleRequest request) {
        return roleService.createRole(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDetailResponse updateRole(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        return roleService.updateRole(id, roleRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteRole(@PathVariable Integer id) {
        return roleService.deleteRole(id);
    }
}
