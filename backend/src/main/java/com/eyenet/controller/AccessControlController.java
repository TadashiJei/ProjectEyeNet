package com.eyenet.controller;

import com.eyenet.model.entity.*;
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
    public ResponseEntity<Void> createRole(@Valid @RequestBody Role role) {
        accessControlService.createRole(role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createPermission(@Valid @RequestBody Permission permission) {
        accessControlService.createPermission(permission);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRoleToUser(
            @PathVariable UUID userId,
            @RequestParam String roleName) {
        accessControlService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/roles/{roleName}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignPermissionToRole(
            @PathVariable String roleName,
            @RequestParam String permissionName) {
        accessControlService.assignPermissionToRole(roleName, permissionName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/roles/{roleName}/permissions/{permissionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removePermissionFromRole(
            @PathVariable String roleName,
            @PathVariable String permissionName) {
        accessControlService.removePermissionFromRole(roleName, permissionName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(accessControlService.getAllRoles());
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(accessControlService.getAllPermissions());
    }

    @GetMapping("/roles/{roleName}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<Permission>> getRolePermissions(@PathVariable String roleName) {
        return ResponseEntity.ok(accessControlService.getRolePermissions(roleName));
    }

    @PutMapping("/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRole(
            @PathVariable String roleName,
            @Valid @RequestBody Role roleDetails) {
        accessControlService.updateRole(roleName, roleDetails);
        return ResponseEntity.ok().build();
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
