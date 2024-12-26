package com.eyenet.controller;

import com.eyenet.model.document.PermissionDocument;
import com.eyenet.model.document.RoleDocument;
import com.eyenet.service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/access-control")
@RequiredArgsConstructor
public class AccessControlController {
    private final AccessControlService accessControlService;

    @PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDocument> createRole(@Valid @RequestBody RoleDocument role) {
        RoleDocument createdRole = accessControlService.createRole(
            role.getName(), 
            role.getDescription(), 
            role.getPermissions().stream()
                .map(PermissionDocument::getName)
                .collect(java.util.stream.Collectors.toSet())
        );
        return ResponseEntity.ok(createdRole);
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionDocument> createPermission(@Valid @RequestBody PermissionDocument permission) {
        PermissionDocument createdPermission = accessControlService.createPermission(
            permission.getName(), 
            permission.getDescription(), 
            permission.getResource(), 
            permission.getAction()
        );
        return ResponseEntity.ok(createdPermission);
    }

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRoleToUser(
            @PathVariable UUID userId,
            @RequestParam String roleName) {
        accessControlService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDocument> addPermissionToRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        RoleDocument updatedRole = accessControlService.addPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDocument> removePermissionFromRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        RoleDocument updatedRole = accessControlService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok(updatedRole);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDocument>> getAllRoles() {
        return ResponseEntity.ok(accessControlService.getAllRoles());
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionDocument>> getAllPermissions() {
        return ResponseEntity.ok(accessControlService.getAllPermissions());
    }

    @GetMapping("/roles/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<PermissionDocument>> getRolePermissions(@PathVariable UUID roleId) {
        return ResponseEntity.ok(accessControlService.getRolePermissions(roleId));
    }

    @PutMapping("/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDocument> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody RoleDocument roleDetails) {
        RoleDocument updatedRole = accessControlService.updateRole(roleId, roleDetails);
        return ResponseEntity.ok(updatedRole);
    }

    @PostMapping("/users/{userId}/departments/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignUserToDepartment(
            @PathVariable UUID userId,
            @PathVariable UUID departmentId) {
        accessControlService.assignUserToDepartment(userId, departmentId);
        return ResponseEntity.ok().build();
    }
}
