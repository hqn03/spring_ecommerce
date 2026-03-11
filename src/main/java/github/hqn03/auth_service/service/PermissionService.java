package github.hqn03.auth_service.service;

import github.hqn03.auth_service.model.Permission;
import github.hqn03.auth_service.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    Set<Permission> findAllByIds(Set<Integer> ids) {
        return new HashSet<>(permissionRepository.findAllById(ids));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "permissions")
    public List<Permission> getAll(){
        log.info("GET TO ALL PERMISSIONS");
        return permissionRepository.findAll();
    }
}
