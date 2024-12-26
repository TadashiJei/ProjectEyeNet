package com.eyenet.service;

import com.eyenet.mapper.UserMapper;
import com.eyenet.model.document.*;
import com.eyenet.model.dto.*;
import com.eyenet.repository.*;
import com.eyenet.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {
    private final UserDocumentRepository userRepo;
    private final UserActivityDocumentRepository activityRepo;
    private final UserDeviceDocumentRepository deviceRepo;
    private final UserSessionDocumentRepository sessionRepo;
    private final UserPreferencesDocumentRepository preferencesRepo;
    private final UserNotificationDocumentRepository notificationRepo;
    private final UserNetworkUsageDocumentRepository networkUsageRepo;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    public UserProfileDTO getCurrentUserProfile() {
        UserDocument user = securityUtils.getCurrentUser();
        return userMapper.toProfileDTO(user);
    }

    public UserProfileDTO updateProfile(UserProfileUpdateRequest request) {
        UserDocument currentUser = securityUtils.getCurrentUser();
        
        currentUser.setFirstName(request.getFirstName());
        currentUser.setLastName(request.getLastName());
        currentUser.setEmail(request.getEmail());
        currentUser.setPhoneNumber(request.getPhoneNumber());
        currentUser.setJobTitle(request.getJobTitle());
        currentUser.setUpdatedAt(LocalDateTime.now());
        
        if (request.getProfilePicture() != null) {
            currentUser.setProfilePicture(request.getProfilePicture());
        }
        
        UserDocument updatedUser = userRepo.save(currentUser);
        return userMapper.toProfileDTO(updatedUser);
    }

    public void changePassword(PasswordChangeRequest request) {
        UserDocument currentUser = securityUtils.getCurrentUser();
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }
        
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        currentUser.setUpdatedAt(LocalDateTime.now());
        userRepo.save(currentUser);
        
        // Log the password change
        UserActivityDocument activity = UserActivityDocument.builder()
                .id(UUID.randomUUID())
                .userId(currentUser.getId())
                .activityType("PASSWORD_CHANGE")
                .timestamp(LocalDateTime.now())
                .build();
        activityRepo.save(activity);
    }

    public List<UserActivityDocument> getCurrentUserActivity() {
        return activityRepo.findByUserIdOrderByTimestampDesc(securityUtils.getCurrentUser().getId());
    }

    public UserNetworkUsageDocument getCurrentUserNetworkUsage() {
        return networkUsageRepo.findByUserId(securityUtils.getCurrentUser().getId())
            .orElseThrow(() -> new IllegalArgumentException("Network usage data not found"));
    }

    public UserPreferencesDocument getCurrentUserPreferences() {
        return preferencesRepo.findByUserId(securityUtils.getCurrentUser().getId())
            .orElseGet(() -> {
                UserPreferencesDocument preferences = UserPreferencesDocument.builder()
                        .id(UUID.randomUUID())
                        .userId(securityUtils.getCurrentUser().getId())
                        .build();
                return preferencesRepo.save(preferences);
            });
    }

    public UserPreferencesDocument updatePreferences(UserPreferences preferences) {
        UserPreferencesDocument existing = getCurrentUserPreferences();
        existing.setEmailNotifications(preferences.isEmailNotifications());
        existing.setSmsNotifications(preferences.isSmsNotifications());
        existing.setInAppNotifications(preferences.isInAppNotifications());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return preferencesRepo.save(existing);
    }

    public List<UserNotificationDocument> getCurrentUserNotifications() {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(securityUtils.getCurrentUser().getId());
    }

    public void markNotificationAsRead(UUID notificationId) {
        UserDocument currentUser = securityUtils.getCurrentUser();
        UserNotificationDocument notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        
        if (!notification.getUserId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Cannot mark another user's notification as read");
        }
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepo.save(notification);
    }

    public List<UserDeviceDocument> getCurrentUserDevices() {
        return deviceRepo.findByUserId(securityUtils.getCurrentUser().getId());
    }

    public UserDeviceDocument registerDevice(UserDeviceRegistration request) {
        UserDocument user = securityUtils.getCurrentUser();
        
        UserDeviceDocument device = UserDeviceDocument.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .deviceName(request.getDeviceName())
                .deviceType(request.getDeviceType())
                .macAddress(request.getMacAddress())
                .trusted(request.isTrusted())
                .lastUsedAt(LocalDateTime.now())
                .build();
        
        device = deviceRepo.save(device);
        
        // Log the device registration
        UserActivityDocument activity = UserActivityDocument.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .activityType("DEVICE_REGISTER")
                .details("Device ID: " + device.getId())
                .timestamp(LocalDateTime.now())
                .build();
        activityRepo.save(activity);
        
        return device;
    }

    public void removeDevice(UUID deviceId) {
        UserDeviceDocument device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        
        if (!device.getUserId().equals(securityUtils.getCurrentUser().getId())) {
            throw new IllegalArgumentException("Cannot remove another user's device");
        }
        
        device.setTerminated(true);
        device.setTerminatedAt(LocalDateTime.now());
        deviceRepo.save(device);
        
        // Log the device removal
        UserActivityDocument activity = UserActivityDocument.builder()
                .id(UUID.randomUUID())
                .userId(device.getUserId())
                .activityType("DEVICE_REMOVE")
                .details("Device ID: " + device.getId())
                .timestamp(LocalDateTime.now())
                .build();
        activityRepo.save(activity);
    }

    public List<UserSessionDocument> getCurrentUserSessions() {
        return sessionRepo.findByUserIdOrderByLastAccessedDesc(securityUtils.getCurrentUser().getId());
    }

    public void terminateSession(UUID sessionId) {
        UserDocument currentUser = securityUtils.getCurrentUser();
        UserSessionDocument session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        
        if (!session.getUserId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Cannot terminate another user's session");
        }
        
        session.setTerminated(true);
        session.setTerminatedAt(LocalDateTime.now());
        sessionRepo.save(session);
        
        // Log the session termination
        UserActivityDocument activity = UserActivityDocument.builder()
                .id(UUID.randomUUID())
                .userId(currentUser.getId())
                .activityType("SESSION_TERMINATED")
                .details("Session ID: " + sessionId)
                .timestamp(LocalDateTime.now())
                .build();
        activityRepo.save(activity);
    }

    public void terminateAllOtherSessions() {
        String currentSession = securityUtils.getCurrentSession();
        sessionRepo.terminateAllExcept(securityUtils.getCurrentUser().getId(), currentSession);
    }
}
