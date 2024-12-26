package com.eyenet.service;

import com.eyenet.model.document.PasswordPolicyDocument;
import com.eyenet.model.document.PasswordResetDocument;
import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.mongodb.PasswordPolicyRepository;
import com.eyenet.repository.mongodb.PasswordResetRepository;
import com.eyenet.repository.mongodb.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagementServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetRepository passwordResetRepository;

    @Mock
    private PasswordPolicyRepository passwordPolicyRepository;

    @InjectMocks
    private PasswordManagementService passwordManagementService;

    private UserDocument testUser;
    private PasswordPolicyDocument testPolicy;
    private PasswordResetDocument testReset;
    private UUID userId;
    private UUID policyId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        policyId = UUID.randomUUID();

        testUser = new UserDocument();
        testUser.setId(userId);
        testUser.setPassword("hashedPassword");

        testPolicy = PasswordPolicyDocument.builder()
                .id(policyId)
                .minLength(8)
                .maxLength(20)
                .requireUppercase(true)
                .requireLowercase(true)
                .requireNumbers(true)
                .requireSpecialChars(true)
                .maxAgeDays(90)
                .build();

        testReset = PasswordResetDocument.builder()
                .id(1L)
                .token("testToken")
                .userId(userId)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();
    }

    @Test
    void changePassword_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("currentPassword", testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("newPassword", testUser.getPassword())).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");

        passwordManagementService.changePassword(userId, "currentPassword", "newPassword");

        verify(userRepository).save(any(UserDocument.class));
    }

    @Test
    void changePassword_UserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> passwordManagementService.changePassword(userId, "currentPassword", "newPassword"));
    }

    @Test
    void changePassword_IncorrectCurrentPassword() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", testUser.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> passwordManagementService.changePassword(userId, "wrongPassword", "newPassword"));
    }

    @Test
    void initiatePasswordReset_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(passwordResetRepository.save(any(PasswordResetDocument.class))).thenReturn(testReset);

        String token = passwordManagementService.initiatePasswordReset(userId);

        assertNotNull(token);
        verify(passwordResetRepository).save(any(PasswordResetDocument.class));
    }

    @Test
    void resetPassword_Success() {
        when(passwordResetRepository.findByToken("testToken")).thenReturn(Optional.of(testReset));
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");

        passwordManagementService.resetPassword("testToken", "newPassword");

        verify(userRepository).save(any(UserDocument.class));
        verify(passwordResetRepository).save(any(PasswordResetDocument.class));
        assertTrue(testReset.isUsed());
        assertNotNull(testReset.getUsedAt());
    }

    @Test
    void resetPassword_InvalidToken() {
        when(passwordResetRepository.findByToken("invalidToken")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> passwordManagementService.resetPassword("invalidToken", "newPassword"));
    }

    @Test
    void validatePasswordStrength_Success() {
        when(passwordPolicyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));

        boolean result = passwordManagementService.validatePasswordStrength("Test1234!", policyId);

        assertTrue(result);
    }

    @Test
    void validatePasswordStrength_TooShort() {
        when(passwordPolicyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));

        assertThrows(IllegalArgumentException.class,
                () -> passwordManagementService.validatePasswordStrength("Test1!", policyId));
    }

    @Test
    void validatePasswordStrength_NoUppercase() {
        when(passwordPolicyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));

        assertThrows(IllegalArgumentException.class,
                () -> passwordManagementService.validatePasswordStrength("test1234!", policyId));
    }

    @Test
    void validatePasswordStrength_NoLowercase() {
        when(passwordPolicyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));

        assertThrows(IllegalArgumentException.class,
                () -> passwordManagementService.validatePasswordStrength("TEST1234!", policyId));
    }

    @Test
    void validatePasswordStrength_NoNumbers() {
        when(passwordPolicyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));

        assertThrows(IllegalArgumentException.class,
                () -> passwordManagementService.validatePasswordStrength("TestTest!", policyId));
    }

    @Test
    void validatePasswordStrength_NoSpecialChars() {
        when(passwordPolicyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));

        assertThrows(IllegalArgumentException.class,
                () -> passwordManagementService.validatePasswordStrength("Test1234", policyId));
    }
}
