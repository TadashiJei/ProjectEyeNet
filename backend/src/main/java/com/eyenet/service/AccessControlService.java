package com.eyenet.service;

import com.eyenet.model.document.DepartmentDocument;
import com.eyenet.model.document.PermissionDocument;
import com.eyenet.model.document.RoleDocument;
import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.DepartmentDocumentRepository;
import com.eyenet.repository.PermissionDocumentRepository;
import com.eyenet.repository.RoleDocumentRepository;
import com.eyenet.repository.UserDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccessControlService {
    private final UserDocumentRepository userRepository;
    private final RoleDocumentRepository roleRepository;
    private final PermissionDocumentRepository permissionRepository;
    private final DepartmentDocumentRepository departmentRepository;

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
        role.getPermissions().add(permission.getName());
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
                                role.getPermissions().contains(permissionName));
    }

    public Set<String> getUserPermissions(UUID userId) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return Collections.emptySet();
        }
        
        Set<String> permissions = new HashSet<>();
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
    public RoleDocument createRole(String name, String description, Set<String> permissions) {
        if (roleRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Role already exists");
        }
        
        RoleDocument role = RoleDocument.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .permissions(permissions)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return roleRepository.save(role);
    }

    @Transactional
    public PermissionDocument createPermission(String name, String description, 
                                             String resource, String action) {
        if (permissionRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Permission already exists");
        }
        
        PermissionDocument permission = PermissionDocument.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .resource(resource)
                .action(action)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return permissionRepository.save(permission);
    }
}
