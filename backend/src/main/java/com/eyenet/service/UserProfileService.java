package com.eyenet.service;

import com.eyenet.mapper.UserMapper;
import com.eyenet.model.document.*;
import com.eyenet.model.dto.*;
import com.eyenet.model.entity.*;
import com.eyenet.repository.*;
import com.eyenet.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {
    private final UserRepository userRepo;
    private final UserActivityRepository activityRepo;
    private final UserDeviceRepository deviceRepo;
    private final UserSessionRepository sessionRepo;
    private final UserPreferencesRepository preferencesRepo;
    private final UserNotificationRepository notificationRepo;
    private final UserNetworkUsageRepository networkUsageRepo;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    public UserProfileDTO getCurrentUserProfile() {
        User user = securityUtils.getCurrentUser();
        UserProfileDTO profile = userMapper.mapToUserProfileDTO(user);
        return profile;
    }

    public UserProfileDTO updateProfile(UserProfileUpdateRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        UserDocument userDoc = userMapper.mapToUserDocument(currentUser);
        
        userDoc.setFirstName(request.getFirstName());
        userDoc.setLastName(request.getLastName());
        userDoc.setEmail(request.getEmail());
        userDoc.setPhoneNumber(request.getPhoneNumber());
        userDoc.setJobTitle(request.getJobTitle());
        
        if (request.getProfilePicture() != null) {
            userDoc.setProfilePicture(request.getProfilePicture());
        }
        
        userDoc = userRepo.save(userDoc);
        User updatedUser = userMapper.mapToUser(userDoc);
        
        // Log activity
        UserActivity activity = new UserActivity();
        activity.setUser(updatedUser);
        activity.setActivityType(UserActivity.ActivityType.PROFILE_UPDATE);
        activity.setDescription("Profile updated");
        activityRepo.save(activity);
        
        return userMapper.mapToUserProfileDTO(updatedUser);
    }

    public void changePassword(PasswordChangeRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        UserDocument userDoc = userMapper.mapToUserDocument(currentUser);
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), userDoc.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }
        
        userDoc.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(userDoc);
        
        // Log activity
        UserActivity activity = new UserActivity();
        activity.setUser(currentUser);
        activity.setActivityType(UserActivity.ActivityType.PASSWORD_CHANGE);
        activity.setDescription("Password changed");
        activityRepo.save(activity);
    }

    public List<UserActivity> getCurrentUserActivity() {
        return activityRepo.findByUserOrderByCreatedAtDesc(securityUtils.getCurrentUser());
    }

    public UserNetworkUsage getCurrentUserNetworkUsage() {
        return networkUsageRepo.findLatestByUser(securityUtils.getCurrentUser())
            .orElseThrow(() -> new EntityNotFoundException("No network usage data found"));
    }

    public UserPreferences getCurrentUserPreferences() {
        return preferencesRepo.findByUser(securityUtils.getCurrentUser())
            .orElseGet(() -> {
                UserPreferences preferences = new UserPreferences();
                preferences.setUser(securityUtils.getCurrentUser());
                return preferencesRepo.save(preferences);
            });
    }

    public UserPreferences updatePreferences(UserPreferences preferences) {
        UserPreferences existing = getCurrentUserPreferences();
        existing.setNotificationEnabled(preferences.isNotificationEnabled());
        existing.setEmailNotification(preferences.isEmailNotification());
        existing.setDashboardTheme(preferences.getDashboardTheme());
        existing.setLanguage(preferences.getLanguage());
        existing.setTimezone(preferences.getTimezone());
        existing.setItemsPerPage(preferences.getItemsPerPage());
        return preferencesRepo.save(existing);
    }

    public List<UserNotification> getCurrentUserNotifications() {
        return notificationRepo.findByUserOrderByCreatedAtDesc(securityUtils.getCurrentUser());
    }

    public void markNotificationAsRead(UUID notificationId) {
        UserNotification notification = notificationRepo.findById(notificationId)
            .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        
        if (!notification.getUser().equals(securityUtils.getCurrentUser())) {
            throw new IllegalArgumentException("Not authorized to modify this notification");
        }
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepo.save(notification);
    }

    public void markAllNotificationsAsRead() {
        notificationRepo.markAllAsRead(securityUtils.getCurrentUser().getId(), LocalDateTime.now());
    }

    public List<UserDevice> getCurrentUserDevices() {
        return deviceRepo.findByUserAndEnabled(securityUtils.getCurrentUser(), true);
    }

    public UserDevice registerDevice(UserDeviceRegistration request) {
        User user = securityUtils.getCurrentUser();
        
        UserDevice device = new UserDevice();
        device.setUser(user);
        device.setDeviceName(request.getDeviceName());
        device.setDeviceType(request.getDeviceType());
        device.setMacAddress(request.getMacAddress());
        device.setTrusted(request.isTrusted());
        device.setLastUsedAt(LocalDateTime.now());
        
        device = deviceRepo.save(device);
        
        // Log activity
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setActivityType(UserActivity.ActivityType.DEVICE_REGISTER);
        activity.setDescription("New device registered: " + request.getDeviceName());
        activityRepo.save(activity);
        
        return device;
    }

    public void removeDevice(UUID deviceId) {
        UserDevice device = deviceRepo.findById(deviceId)
            .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        
        if (!device.getUser().equals(securityUtils.getCurrentUser())) {
            throw new IllegalArgumentException("Not authorized to remove this device");
        }
        
        device.setEnabled(false);
        deviceRepo.save(device);
        
        // Log activity
        UserActivity activity = new UserActivity();
        activity.setUser(device.getUser());
        activity.setActivityType(UserActivity.ActivityType.DEVICE_REMOVE);
        activity.setDescription("Device removed: " + device.getDeviceName());
        activityRepo.save(activity);
    }

    public List<UserSession> getCurrentUserSessions() {
        return sessionRepo.findByUserAndActiveOrderByCreatedAtDesc(
            securityUtils.getCurrentUser(), true);
    }

    public void terminateSession(UUID sessionId) {
        UserSession session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        
        if (!session.getUser().equals(securityUtils.getCurrentUser())) {
            throw new IllegalArgumentException("Not authorized to terminate this session");
        }
        
        session.setActive(false);
        sessionRepo.save(session);
    }

    public void terminateAllOtherSessions() {
        String currentSession = securityUtils.getCurrentSession();
        sessionRepo.terminateAllExcept(securityUtils.getCurrentUser().getId(), currentSession);
    }
}
