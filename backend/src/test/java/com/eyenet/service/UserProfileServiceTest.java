package com.eyenet.service;

import com.eyenet.model.dto.*;
import com.eyenet.model.entity.*;
import com.eyenet.repository.*;
import com.eyenet.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserRepository userRepo;
    
    @Mock
    private UserActivityRepository activityRepo;
    
    @Mock
    private UserDeviceRepository deviceRepo;
    
    @Mock
    private UserSessionRepository sessionRepo;
    
    @Mock
    private UserPreferencesRepository preferencesRepo;
    
    @Mock
    private UserNotificationRepository notificationRepo;
    
    @Mock
    private UserNetworkUsageRepository networkUsageRepo;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserProfileService userProfileService;

    private User testUser;
    private UserSession testSession;

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
        
        when(userRepo.save(any(User.class))).thenReturn(testUser);

        // Act
        UserProfileDTO result = userProfileService.updateProfile(request);

        // Assert
        verify(userRepo).save(any(User.class));
        verify(activityRepo).save(any(UserActivity.class));
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
        verify(userRepo).save(any(User.class));
        verify(activityRepo).save(any(UserActivity.class));
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
        
        UserDevice device = new UserDevice();
        device.setId(UUID.randomUUID());
        when(deviceRepo.save(any(UserDevice.class))).thenReturn(device);

        // Act
        UserDevice result = userProfileService.registerDevice(request);

        // Assert
        verify(deviceRepo).save(any(UserDevice.class));
        verify(activityRepo).save(any(UserActivity.class));
        assertThat(result).isNotNull();
    }

    @Test
    void removeDevice_ShouldDisableDeviceAndLogActivity() {
        // Arrange
        UUID deviceId = UUID.randomUUID();
        UserDevice device = new UserDevice();
        device.setId(deviceId);
        device.setUser(testUser);
        device.setDeviceName("Test Device");
        
        when(deviceRepo.findById(deviceId)).thenReturn(Optional.of(device));

        // Act
        userProfileService.removeDevice(deviceId);

        // Assert
        verify(deviceRepo).save(device);
        verify(activityRepo).save(any(UserActivity.class));
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

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("encoded_password");
        user.setEnabled(true);
        return user;
    }

    private UserSession createTestSession(User user) {
        UserSession session = new UserSession();
        session.setId(UUID.randomUUID());
        session.setUser(user);
        session.setSessionToken("test-token");
        session.setActive(true);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusHours(1));
        return session;
    }
}
