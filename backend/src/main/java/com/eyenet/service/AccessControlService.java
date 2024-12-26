package com.eyenet.service;

import com.eyenet.model.document.DepartmentDocument;
import com.eyenet.model.document.PermissionDocument;
import com.eyenet.model.document.RoleDocument;
import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.mongodb.DepartmentRepository;
import com.eyenet.repository.mongodb.PermissionRepository;
import com.eyenet.repository.mongodb.RoleRepository;
import com.eyenet.repository.mongodb.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessControlService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void assignRoleToUser(UUID userId, String roleName) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        RoleDocument role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role.getName());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void assignPermissionToRole(String roleName, String permissionName) {
        RoleDocument role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        PermissionDocument permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
        
        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }
        role.getPermissions().add(permission);
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }

    @Transactional
    public void assignUserToDepartment(UUID userId, UUID departmentId) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        DepartmentDocument department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        
        user.setDepartmentId(department.getId());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public boolean hasPermission(UUID userId, String permissionName) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return false;
        }
        
        List<RoleDocument> userRoles = roleRepository.findByNameIn(user.getRoles());
        return userRoles.stream()
                .anyMatch(role -> role.getPermissions() != null && 
                                role.getPermissions().stream()
                                    .anyMatch(permission -> permission.getName().equals(permissionName)));
    }

    public Set<PermissionDocument> getUserPermissions(UUID userId) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return Collections.emptySet();
        }
        
        Set<PermissionDocument> permissions = new HashSet<>();
        List<RoleDocument> userRoles = roleRepository.findByNameIn(user.getRoles());
        
        userRoles.forEach(role -> {
            if (role.getPermissions() != null) {
                permissions.addAll(role.getPermissions());
            }
        });
        
        return permissions;
    }

    public void checkPermission(String requiredPermission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }
        
        UserDocument user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("User not found"));
        
        if (!hasPermission(user.getId(), requiredPermission)) {
            throw new AccessDeniedException("Missing required permission: " + requiredPermission);
        }
    }

    @Transactional
    public RoleDocument createRole(String name, String description, Set<String> permissionNames) {
        // Validate permissions exist
        List<PermissionDocument> permissionDocs = permissionRepository.findByNameIn(permissionNames);
        if (permissionDocs.size() != permissionNames.size()) {
            throw new IllegalArgumentException("Some permissions do not exist");
        }

        RoleDocument role = RoleDocument.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .permissions(new HashSet<>(permissionDocs))
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return roleRepository.save(role);
    }

    @Transactional
    public PermissionDocument createPermission(String name, String description, String resource, String action) {
        PermissionDocument permission = PermissionDocument.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .resource(resource)
                .action(action)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return permissionRepository.save(permission);
    }

    @Transactional
    public RoleDocument addPermissionToRole(UUID roleId, UUID permissionId) {
        RoleDocument role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        
        PermissionDocument permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));

        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }
        role.getPermissions().add(permission);
        role.setUpdatedAt(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Transactional
    public RoleDocument removePermissionFromRole(UUID roleId, UUID permissionId) {
        RoleDocument role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        
        role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
        role.setUpdatedAt(LocalDateTime.now());
        return roleRepository.save(role);
    }

    public List<RoleDocument> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<PermissionDocument> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Set<PermissionDocument> getRolePermissions(UUID roleId) {
        RoleDocument role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        return role.getPermissions();
    }

    @Transactional
    public RoleDocument updateRole(UUID roleId, RoleDocument updatedRole) {
        RoleDocument existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        existingRole.setName(updatedRole.getName());
        existingRole.setDescription(updatedRole.getDescription());
        existingRole.setPermissions(updatedRole.getPermissions());
        existingRole.setEnabled(updatedRole.isEnabled());
        existingRole.setUpdatedAt(LocalDateTime.now());

        return roleRepository.save(existingRole);
    }

    @Transactional
    public void deleteRole(UUID roleId) {
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public void deletePermission(UUID permissionId) {
        // First remove this permission from all roles
        List<RoleDocument> roles = roleRepository.findAll();
        for (RoleDocument role : roles) {
            role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
            roleRepository.save(role);
        }

        // Then delete the permission
        permissionRepository.deleteById(permissionId);
    }
}
