package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.role.*;
import github.hqn03.auth_service.exception.AppException;
import github.hqn03.auth_service.exception.ResourceNotFoundException;
import github.hqn03.auth_service.mapper.RoleMapper;
import github.hqn03.auth_service.model.Permission;
import github.hqn03.auth_service.model.Role;
import github.hqn03.auth_service.repository.PermissionRepository;
import github.hqn03.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionService permissionService;

    @Transactional(readOnly = true)
    public Role findById(Integer id){
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Set<Role> findAllByIds(Set<Integer> roleIds) {
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }

    @Transactional(readOnly = true)
    public Role getUserRole(){
        return roleRepository.findByName("USER").orElseThrow(() ->  new ResourceNotFoundException("Role not found"));
    }

    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleDetailResponse getRole(Integer id){
        Role role = this.findById(id);
        return roleMapper.toRoleDetailResponse(role);
    }

    public RoleDetailResponse createRole(RoleRequest request){
        if (roleRepository.existsByName(request.name())){
            throw new AppException("Role name already exists", HttpStatus.BAD_REQUEST);
        }

        Role role = roleMapper.toEntity(request);

        if (request.permissionIds() != null && !request.permissionIds().isEmpty()) {
            Set<Permission> permissions = permissionService.findAllByIds(request.permissionIds());
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
    public RoleDetailResponse updateRole(Integer id, RoleRequest request){
        Role role = this.findById(id);

        roleMapper.updateRoleFromRequest(request, role);

        if(request.permissionIds() != null){
            Set<Permission> permissions = permissionService.findAllByIds(request.permissionIds());
            if(permissions.size() != request.permissionIds().size()){
                throw new ResourceNotFoundException("Some permission IDs are invalid");
            }
            role.setPermissions(permissions);
        }

        Role updated = roleRepository.save(role);
        return roleMapper.toRoleDetailResponse(updated);
    }

    @Transactional
    public String deleteRole(Integer id){
        Role role = this.findById(id);

        roleRepository.delete(role);
        return "Role Deleted Successfully";
    }
}
