package com.eyenet.controller;

import com.eyenet.model.document.UserDocument;
import com.eyenet.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserManagementService userManagementService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDocument>> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDocument> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userManagementService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDocument> createUser(@Valid @RequestBody UserDocument user) {
        return ResponseEntity.ok(userManagementService.createUser(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDocument> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserDocument user) {
        return ResponseEntity.ok(userManagementService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userManagementService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDocument> activateUser(@PathVariable String id) {
        return ResponseEntity.ok(userManagementService.activateUser(id));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDocument> deactivateUser(@PathVariable String id) {
        return ResponseEntity.ok(userManagementService.deactivateUser(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDocument>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role) {
        return ResponseEntity.ok(userManagementService.searchUsers(username, email, role));
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetUserPassword(@PathVariable String id) {
        return ResponseEntity.ok(userManagementService.resetUserPassword(id));
    }

    @GetMapping("/{id}/activity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getUserActivity(
            @PathVariable String id,
            @RequestParam(required = false) String period) {
        return ResponseEntity.ok(userManagementService.getUserActivity(id, period));
    }
}
