package com.eyenet.controller;

import com.eyenet.model.entity.PasswordPolicy;
import com.eyenet.service.PasswordManagementService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/password-management")
@RequiredArgsConstructor
public class PasswordManagementController {
    private final PasswordManagementService passwordManagementService;

    @PostMapping("/policies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PasswordPolicy> createPolicy(@Valid @RequestBody PasswordPolicy policy) {
        return ResponseEntity.ok(passwordManagementService.createPolicy(policy));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            @RequestParam UUID userId,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        passwordManagementService.changePassword(userId, currentPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/initiate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam UUID userId) {
        String token = passwordManagementService.initiatePasswordReset(userId);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reset-password/complete")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        passwordManagementService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> validatePasswordStrength(
            @RequestParam String password,
            @RequestParam UUID policyId) {
        boolean isValid = passwordManagementService.validatePasswordStrength(password, policyId);
        return ResponseEntity.ok(isValid);
    }
}
