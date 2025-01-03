package com.eyenet.service;

import com.eyenet.model.document.PasswordPolicyDocument;
import com.eyenet.model.document.PasswordResetDocument;
import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.mongodb.PasswordPolicyRepository;
import com.eyenet.repository.mongodb.PasswordResetRepository;
import com.eyenet.repository.mongodb.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordManagementService {
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordPolicyRepository passwordPolicyRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public String initiatePasswordReset(UUID userId) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String token = generateResetToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        PasswordResetDocument reset = PasswordResetDocument.builder()
                .token(token)
                .userId(userId)
                .expiresAt(expiresAt)
                .used(false)
                .build();

        passwordResetRepository.save(reset);
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetDocument reset = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid or expired token"));

        if (reset.isExpired() || reset.isUsed()) {
            throw new IllegalArgumentException("Token has expired or already been used");
        }

        UserDocument user = userRepository.findById(reset.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        reset.setUsed(true);
        reset.setUsedAt(LocalDateTime.now());
        passwordResetRepository.save(reset);
    }

    public void initiatePasswordReset(String email) {
        UserDocument user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(24);

        // Save reset token
        PasswordResetDocument resetDocument = PasswordResetDocument.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .token(resetToken)
                .expiryTime(expiryTime)
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        passwordResetRepository.save(resetDocument);
    }

    public void resetPassword(UUID userId, String newPassword) {
        PasswordResetDocument resetDocument = passwordResetRepository.findByUserIdAndUsedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (resetDocument.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        // Update user's password
        UserDocument user = userRepository.findById(resetDocument.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mark token as used
        resetDocument.setUsed(true);
        resetDocument.setUpdatedAt(LocalDateTime.now());
        passwordResetRepository.save(resetDocument);
    }

    public void validateResetToken(String token) {
        PasswordResetDocument resetDocument = passwordResetRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (resetDocument.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }
    }

    private String generateResetToken() {
        return RandomStringUtils.randomAlphanumeric(32);
    }

    public boolean validatePasswordStrength(String password, UUID policyId) {
        PasswordPolicyDocument policy = passwordPolicyRepository.findById(policyId)
                .orElseThrow(() -> new EntityNotFoundException("Password policy not found"));

        if (password.length() < policy.getMinLength()) {
            throw new IllegalArgumentException("Password must be at least " + policy.getMinLength() + " characters long");
        }

        if (policy.isRequireUppercase() && !password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if (policy.isRequireLowercase() && !password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }

        if (policy.isRequireNumbers() && !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }

        if (policy.isRequireSpecialChars() && !password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }

        return true;
    }
}
