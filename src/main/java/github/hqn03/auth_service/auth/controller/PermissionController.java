package github.hqn03.auth_service.auth.controller;

import github.hqn03.auth_service.auth.entity.Permission;
import github.hqn03.auth_service.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasAuthority('ROLE:CREATE')")
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Permission> getAll() {
        return permissionService.getAll();
    }
}
