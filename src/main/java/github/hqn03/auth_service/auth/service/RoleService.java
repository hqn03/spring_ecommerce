package github.hqn03.auth_service.auth.service;

import github.hqn03.auth_service.auth.dto.role.RoleDetailResponse;
import github.hqn03.auth_service.auth.dto.role.RoleRequest;
import github.hqn03.auth_service.auth.dto.role.RoleResponse;
import github.hqn03.auth_service.auth.entity.Permission;
import github.hqn03.auth_service.auth.entity.Role;
import github.hqn03.auth_service.auth.mapper.RoleMapper;
import github.hqn03.auth_service.auth.repository.PermissionRepository;
import github.hqn03.auth_service.auth.repository.RoleRepository;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public Set<Role> findAllByIds(Set<Integer> roleIds) {
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }

    public Role getRoleUser() {
        return roleRepository.findByName("USER").orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public Role getRoleGuest() {
        return roleRepository.findByName("GUEST").orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public RoleDetailResponse getRole(Integer id) {
        Role role = roleRepository.findWithPermissionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found"));
        return roleMapper.toRoleDetailResponse(role);
    }

    @Transactional
    public RoleDetailResponse createRole(RoleRequest request) {
         if(roleRepository.existsByName(request.name()))
             throw new AppException("Role name is already exists", HttpStatus.BAD_REQUEST);

        Role role = roleMapper.toEntity(request);

        if (request.permissionIds() != null && !request.permissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.permissionIds()));
            // Kiểm tra tính hợp lệ của ID
            if (permissions.size() != request.permissionIds().size()) {
                throw new ResourceNotFoundException("Some permission IDs are invalid");
            }
            role.setPermissions(permissions);
        }
        Role saved = roleRepository.save(role);
        return roleMapper.toRoleDetailResponse(saved);
    }

    @Transactional
    public RoleDetailResponse updateRole(Integer id, RoleRequest request) {
        if(roleRepository.existsByNameAndIdNot(request.name(), id))
            throw new AppException("Role name is already exists", HttpStatus.BAD_REQUEST);

        Role role = roleRepository.findWithPermissionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found"));

        roleMapper.updateRoleFromRequest(request, role);

        if (request.permissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.permissionIds()));
            if (permissions.size() != request.permissionIds().size()) {
                throw new ResourceNotFoundException("Some permission IDs are invalid");
            }
            role.setPermissions(permissions);
        }

        Role updated = roleRepository.save(role);
        return roleMapper.toRoleDetailResponse(updated);
    }

    @Transactional
    public String deleteRole(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found"));

        roleRepository.delete(role);
        return "Role Deleted Successfully";
    }
}
