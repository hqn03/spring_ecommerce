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
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public List<RoleResponse> getAll(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public RoleDetailResponse getRole(Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));

        return roleMapper.toRoleDetailResponse(role);
    }

    public RoleDetailResponse createRole(RoleRequest request){
        if (roleRepository.existsByName(request.name())){
            throw new AppException("Role name already exists", HttpStatus.BAD_REQUEST);
        }

        Role role = roleMapper.toEntity(request);

        if (request.permissionIds() != null && !request.permissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(
                    permissionRepository.findAllById(request.permissionIds())
            );
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
    public RoleDetailResponse updateRole(Long id, RoleRequest request){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));

        roleMapper.updateRoleFromRequest(request, role);

        if(request.permissionIds() != null){
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.permissionIds()));
            if(permissions.size() != request.permissionIds().size()){
                throw new ResourceNotFoundException("Some permission IDs are invalid");
            }
            role.setPermissions(permissions);
        }

        Role updated = roleRepository.save(role);
        return roleMapper.toRoleDetailResponse(updated);
    }

    public String deleteRole(Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role Not Found"));

        roleRepository.delete(role);
        return "Role Deleted Successfully";
    }
}
