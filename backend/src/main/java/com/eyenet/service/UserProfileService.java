package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.model.dto.UserDeviceRegistrationRequest;
import com.eyenet.repository.mongodb.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserNotificationRepository userNotificationRepository;

    public UserDocument getUserProfile(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDocument updateProfile(UUID userId, UserDocument userUpdate) {
        UserDocument currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.setFirstName(userUpdate.getFirstName());
        currentUser.setLastName(userUpdate.getLastName());
        currentUser.setEmail(userUpdate.getEmail());
        currentUser.setPhoneNumber(userUpdate.getPhoneNumber());
        currentUser.setJobTitle(userUpdate.getJobTitle());
        currentUser.setUpdatedAt(LocalDateTime.now());

        if (userUpdate.getProfilePicture() != null) {
            currentUser.setProfilePicture(userUpdate.getProfilePicture());
        }

        return userRepository.save(currentUser);
    }

    public List<UserActivityDocument> getUserActivity(UUID userId) {
        return userActivityRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public List<UserActivityDocument> getUserActivityByType(UUID userId, String type) {
        return userActivityRepository.findByUserIdAndType(userId, type);
    }

    public UserPreferencesDocument getUserPreferences(UUID userId) {
        return userPreferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User preferences not found"));
    }

    public UserPreferencesDocument updatePreferences(UUID userId, UserPreferencesDocument preferences) {
        UserPreferencesDocument existing = userPreferencesRepository.findByUserId(userId)
                .orElse(UserPreferencesDocument.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .build());

        existing.setTheme(preferences.getTheme());
        existing.setLanguage(preferences.getLanguage());
        existing.setTimezone(preferences.getTimezone());
        existing.setNotificationSettings(preferences.getNotificationSettings());
        existing.setDashboardLayout(preferences.getDashboardLayout());
        existing.setCustomSettings(preferences.getCustomSettings());

        return userPreferencesRepository.save(existing);
    }

    public List<UserNotificationDocument> getUnreadNotifications(UUID userId) {
        return userNotificationRepository.findUnreadNotifications(userId);
    }

    public UserNotificationDocument markNotificationAsRead(UUID notificationId) {
        UserNotificationDocument notification = userNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        return userNotificationRepository.save(notification);
    }

    public List<UserDeviceDocument> getUserDevices(UUID userId) {
        return userDeviceRepository.findByUserId(userId);
    }

    public UserDeviceDocument registerDevice(UUID userId, UserDeviceRegistrationRequest request) {
        UserDeviceDocument device = new UserDeviceDocument();
        device.setId(UUID.randomUUID());
        device.setUserId(userId);
        device.setDeviceIdentifier(request.getDeviceIdentifier());
        device.setDeviceType(request.getDeviceType());
        device.setDeviceName(request.getDeviceName());
        device.setOsVersion(request.getOsVersion());
        device.setManufacturer(request.getManufacturer());
        device.setModel(request.getModel());
        device.setStatus("ACTIVE");
        device.setRegisteredAt(LocalDateTime.now());
        device.setLastSeenAt(LocalDateTime.now());
        device.setMetadata(Map.of(
            "registrationIp", request.getDeviceIdentifier(),
            "userAgent", request.getDeviceName()
        ));
        return userDeviceRepository.save(device);
    }

    public void removeDevice(UUID userId, UUID deviceId) {
        UserDeviceDocument device = userDeviceRepository.findByUserIdAndId(userId, deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        device.setTerminated(true);
        device.setTerminatedAt(LocalDateTime.now());
        userDeviceRepository.save(device);
    }

    public List<UserSessionDocument> getActiveSessions(UUID userId) {
        return userSessionRepository.findByUserIdAndActive(userId, true);
    }

    public void terminateSession(UUID userId, UUID sessionId) {
        UserSessionDocument session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("Session does not belong to user");
        }

        session.setTerminated(true);
        session.setTerminatedAt(LocalDateTime.now());
        userSessionRepository.save(session);
    }

    public void terminateAllOtherSessions(UUID userId, String currentSessionToken) {
        List<UserSessionDocument> otherSessions = userSessionRepository.findOtherActiveSessions(userId, currentSessionToken);
        for (UserSessionDocument session : otherSessions) {
            session.setTerminated(true);
            session.setTerminatedAt(LocalDateTime.now());
            session.setActive(false);
            session.setTerminationReason("Terminated by user request");
            userSessionRepository.save(session);
        }
    }
}
