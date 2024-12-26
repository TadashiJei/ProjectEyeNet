package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.model.dto.*;
import com.eyenet.repository.*;
import com.eyenet.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserDocumentRepository userRepo;
    
    @Mock
    private UserActivityDocumentRepository activityRepo;
    
    @Mock
    private UserDeviceDocumentRepository deviceRepo;
    
    @Mock
    private UserSessionDocumentRepository sessionRepo;
    
    @Mock
    private UserPreferencesDocumentRepository preferencesRepo;
    
    @Mock
    private UserNotificationDocumentRepository notificationRepo;
    
    @Mock
    private UserNetworkUsageDocumentRepository networkUsageRepo;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserDocument testUser;
    private UserSessionDocument testSession;

    @BeforeEach
    void setUp() {
        testUser = createTestUser();
        testSession = createTestSession(testUser);
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void getCurrentUserProfile_ShouldMapUserToDTO() {
        // Act
        UserProfileDTO result = userProfileService.getCurrentUserProfile();

        // Assert
        assertThat(result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(result.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(result.getLastName()).isEqualTo(testUser.getLastName());
    }

    @Test
    void updateProfile_ShouldUpdateUserAndLogActivity() {
        // Arrange
        UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setFirstName("Updated");
        request.setLastName("Name");
        request.setEmail("updated@test.com");
        
        when(userRepo.save(any(UserDocument.class))).thenReturn(testUser);

        // Act
        UserProfileDTO result = userProfileService.updateProfile(request);

        // Assert
        verify(userRepo).save(any(UserDocument.class));
        verify(activityRepo).save(any(UserActivityDocument.class));
        assertThat(result).isNotNull();
    }

    @Test
    void changePassword_ShouldValidateAndUpdatePassword() {
        // Arrange
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword("current");
        request.setNewPassword("newpass");
        request.setConfirmPassword("newpass");
        
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        // Act
        userProfileService.changePassword(request);

        // Assert
        verify(userRepo).save(any(UserDocument.class));
        verify(activityRepo).save(any(UserActivityDocument.class));
    }

    @Test
    void changePassword_ShouldThrowException_WhenCurrentPasswordIsIncorrect() {
        // Arrange
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword("wrong");
        request.setNewPassword("newpass");
        request.setConfirmPassword("newpass");
        
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.changePassword(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Current password is incorrect");
    }

    @Test
    void registerDevice_ShouldCreateDeviceAndLogActivity() {
        // Arrange
        UserDeviceRegistration request = new UserDeviceRegistration();
        request.setDeviceName("Test Device");
        request.setDeviceType("Mobile");
        request.setMacAddress("00:11:22:33:44:55");
        
        UserDeviceDocument device = UserDeviceDocument.builder()
            .id(UUID.randomUUID().toString())
            .build();
        when(deviceRepo.save(any(UserDeviceDocument.class))).thenReturn(device);

        // Act
        UserDeviceDocument result = userProfileService.registerDevice(request);

        // Assert
        verify(deviceRepo).save(any(UserDeviceDocument.class));
        verify(activityRepo).save(any(UserActivityDocument.class));
        assertThat(result).isNotNull();
    }

    @Test
    void removeDevice_ShouldDisableDeviceAndLogActivity() {
        // Arrange
        String deviceId = UUID.randomUUID().toString();
        UserDeviceDocument device = UserDeviceDocument.builder()
            .id(deviceId)
            .userId(testUser.getId())
            .deviceName("Test Device")
            .enabled(true)
            .build();
        
        when(deviceRepo.findById(deviceId)).thenReturn(Optional.of(device));

        // Act
        userProfileService.removeDevice(deviceId);

        // Assert
        verify(deviceRepo).save(device);
        verify(activityRepo).save(any(UserActivityDocument.class));
        assertThat(device.isEnabled()).isFalse();
    }

    @Test
    void terminateAllOtherSessions_ShouldTerminateOtherSessions() {
        // Arrange
        when(securityUtils.getCurrentSession()).thenReturn(testSession);

        // Act
        userProfileService.terminateAllOtherSessions();

        // Assert
        verify(sessionRepo).terminateAllExcept(testUser.getId(), testSession.getId());
    }

    private UserDocument createTestUser() {
        return UserDocument.builder()
            .id(UUID.randomUUID().toString())
            .username("testuser")
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .password("encoded_password")
            .enabled(true)
            .build();
    }

    private UserSessionDocument createTestSession(UserDocument user) {
        return UserSessionDocument.builder()
            .id(UUID.randomUUID().toString())
            .userId(user.getId())
            .sessionToken("test-token")
            .createdAt(LocalDateTime.now())
            .lastAccessedAt(LocalDateTime.now())
            .active(true)
            .build();
    }
}
