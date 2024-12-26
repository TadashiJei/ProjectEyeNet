package com.eyenet.service;

import com.eyenet.model.entity.*;
import com.eyenet.repository.jpa.PasswordHistoryRepository;
import com.eyenet.repository.jpa.PasswordPolicyRepository;
import com.eyenet.repository.jpa.PasswordResetRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PasswordManagementService {
    private final PasswordPolicyRepository policyRepository;
    private final PasswordHistoryRepository historyRepository;
    private final PasswordResetRepository resetRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    private static final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public PasswordPolicy createPolicy(PasswordPolicy policy) {
        if (policyRepository.existsByName(policy.getName())) {
            throw new IllegalArgumentException("Policy already exists with name: " + policy.getName());
        }
        return policyRepository.save(policy);
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userService.getUser(userId);
        PasswordPolicy policy = getApplicablePolicy(user);
        
        validateCurrentPassword(user, currentPassword);
        validateNewPassword(user, newPassword, policy);
        
        updatePassword(user, newPassword, PasswordHistory.ChangeReason.USER_REQUESTED);
    }

    @Transactional
    public String initiatePasswordReset(UUID userId) {
        User user = userService.getUser(userId);
        
        // Check for existing valid tokens
        List<PasswordReset> validTokens = resetRepository.findValidTokensByUser(user, LocalDateTime.now());
        validTokens.forEach(token -> token.setUsed(true));
        resetRepository.saveAll(validTokens);
        
        // Generate new reset token
        String token = generateSecureToken();
        PasswordReset reset = PasswordReset.builder()
                .user(user)
                .token(token)
                .expiry(LocalDateTime.now().plusHours(24))
                .build();
        
        resetRepository.save(reset);
        return token;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordReset reset = resetRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid reset token"));
        
        if (reset.isUsed() || reset.getExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reset token is expired or already used");
        }
        
        User user = reset.getUser();
        PasswordPolicy policy = getApplicablePolicy(user);
        validateNewPassword(user, newPassword, policy);
        
        updatePassword(user, newPassword, PasswordHistory.ChangeReason.ADMIN_RESET);
        
        reset.setUsed(true);
        reset.setUsedAt(LocalDateTime.now());
        resetRepository.save(reset);
    }

    @Transactional(readOnly = true)
    public boolean validatePasswordStrength(String password, UUID policyId) {
        PasswordPolicy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new EntityNotFoundException("Policy not found"));
        
        return validatePasswordAgainstPolicy(password, policy);
    }

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void checkPasswordExpirations() {
        // Implementation for checking password expirations
        // and notifying users/admins
    }

    private PasswordPolicy getApplicablePolicy(User user) {
        // Get user-specific or default policy
        return policyRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new IllegalStateException("No default password policy found"));
    }

    private void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
    }

    private void validateNewPassword(User user, String newPassword, PasswordPolicy policy) {
        if (!validatePasswordAgainstPolicy(newPassword, policy)) {
            throw new IllegalArgumentException("Password does not meet policy requirements");
        }
        
        if (historyRepository.isPasswordPreviouslyUsed(user, 
                passwordEncoder.encode(newPassword))) {
            throw new IllegalArgumentException("Password has been used previously");
        }
    }

    private boolean validatePasswordAgainstPolicy(String password, PasswordPolicy policy) {
        if (password.length() < policy.getMinLength()) {
            return false;
        }
        
        if (policy.isRequireUppercase() && !Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }
        
        if (policy.isRequireLowercase() && !Pattern.compile("[a-z]").matcher(password).find()) {
            return false;
        }
        
        if (policy.isRequireNumbers() && !Pattern.compile("\\d").matcher(password).find()) {
            return false;
        }
        
        if (policy.isRequireSpecialChars() && 
            !Pattern.compile("[" + Pattern.quote(policy.getSpecialCharsAllowed()) + "]")
                   .matcher(password).find()) {
            return false;
        }
        
        return true;
    }

    private void updatePassword(User user, String newPassword, PasswordHistory.ChangeReason reason) {
        String salt = generateSalt();
        String hashedPassword = passwordEncoder.encode(newPassword);
        
        // Update user password
        user.setPassword(hashedPassword);
        user.setPasswordUpdatedAt(LocalDateTime.now());
        userService.updateUser(user);
        
        // Record in history
        PasswordHistory history = PasswordHistory.builder()
                .user(user)
                .passwordHash(hashedPassword)
                .salt(salt)
                .changeReason(reason)
                .build();
        
        historyRepository.save(history);
    }

    private String generateSecureToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
