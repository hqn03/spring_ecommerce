package github.hqn03.auth_service.controller;

import github.hqn03.auth_service.dto.role.*;
import github.hqn03.auth_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleResponse> getAll(){
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDetailResponse getRole(@PathVariable Long id){
        return roleService.getRole(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDetailResponse createRole(@RequestBody RoleRequest request){
        return roleService.createRole(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDetailResponse updateRole(@PathVariable Long id, @RequestBody RoleRequest roleRequest){
        return roleService.updateRole(id, roleRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }
}
