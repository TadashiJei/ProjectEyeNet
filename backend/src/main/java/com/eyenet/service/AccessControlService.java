package com.eyenet.service;

import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.Permission;
import com.eyenet.model.entity.Role;
import com.eyenet.model.entity.User;
import com.eyenet.repository.DepartmentRepository;
import com.eyenet.repository.PermissionRepository;
import com.eyenet.repository.RoleRepository;
import com.eyenet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessControlService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void assignRoleToUser(UUID userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        
        user.getRoles().clear();
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void assignPermissionToRole(String roleName, String permissionName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
        
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    @Transactional
    public void createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new IllegalArgumentException("Role already exists");
        }
        roleRepository.save(role);
    }

    @Transactional
    public void createPermission(Permission permission) {
        if (permissionRepository.findByName(permission.getName()).isPresent()) {
            throw new IllegalArgumentException("Permission already exists");
        }
        permissionRepository.save(permission);
    }

    public boolean hasPermission(Authentication authentication, String permission) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(permission));
    }

    public boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    @Transactional
    public void assignUserToDepartment(UUID userId, UUID departmentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        
        user.setDepartment(department);
        userRepository.save(user);
    }

    public boolean canAccessDepartment(Authentication authentication, UUID departmentId) {
        if (hasRole(authentication, "ROLE_ADMIN")) {
            return true;
        }
        
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getDepartment() != null && 
               user.getDepartment().getId().equals(departmentId);
    }

    public void checkDepartmentAccess(Authentication authentication, UUID departmentId) {
        if (!canAccessDepartment(authentication, departmentId)) {
            throw new AccessDeniedException("Access denied to department resources");
        }
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Set<Permission> getRolePermissions(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        return role.getPermissions();
    }

    @Transactional
    public void removePermissionFromRole(String roleName, String permissionName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
        
        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    @Transactional
    public void updateRole(String roleName, Role roleDetails) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        
        role.setDescription(roleDetails.getDescription());
        role.setPermissions(roleDetails.getPermissions());
        roleRepository.save(role);
    }
}
