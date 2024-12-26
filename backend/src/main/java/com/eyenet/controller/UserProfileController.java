package com.eyenet.controller;

import com.eyenet.model.document.*;
import com.eyenet.model.dto.*;
import com.eyenet.service.UserProfileService;
import com.eyenet.security.SecurityUtils;
import com.eyenet.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userMapper.toProfileDTO(user));
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        UserDocument currentUser = securityUtils.getCurrentUserDocument();
        
        // Map update request to UserDocument
        currentUser.setFirstName(request.getFirstName());
        currentUser.setLastName(request.getLastName());
        currentUser.setEmail(request.getEmail());
        currentUser.setPhoneNumber(request.getPhoneNumber());
        currentUser.setJobTitle(request.getJobTitle());
        if (request.getProfilePicture() != null) {
            currentUser.setProfilePicture(request.getProfilePicture());
        }
        
        UserDocument updatedUser = userProfileService.updateProfile(currentUser.getId(), currentUser);
        return ResponseEntity.ok(userMapper.toProfileDTO(updatedUser));
    }

    @GetMapping("/activity")
    public ResponseEntity<List<UserActivityDocument>> getUserActivity() {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.getUserActivity(user.getId()));
    }

    @GetMapping("/activity/{type}")
    public ResponseEntity<List<UserActivityDocument>> getUserActivityByType(@PathVariable String type) {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.getUserActivityByType(user.getId(), type));
    }

    @GetMapping("/preferences")
    public ResponseEntity<UserPreferencesDocument> getUserPreferences() {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.getUserPreferences(user.getId()));
    }

    @PutMapping("/preferences")
    public ResponseEntity<UserPreferencesDocument> updatePreferences(@RequestBody UserPreferencesDocument preferences) {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.updatePreferences(user.getId(), preferences));
    }

    @GetMapping("/notifications/unread")
    public ResponseEntity<List<UserNotificationDocument>> getUnreadNotifications() {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.getUnreadNotifications(user.getId()));
    }

    @PutMapping("/notifications/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable UUID notificationId) {
        userProfileService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/devices")
    public ResponseEntity<List<UserDeviceDocument>> getUserDevices() {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.getUserDevices(user.getId()));
    }

    @PostMapping("/devices")
    public ResponseEntity<UserDeviceDocument> registerDevice(@RequestBody UserDeviceRegistrationRequest request) {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.registerDevice(user.getId(), request));
    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<Void> removeDevice(@PathVariable UUID deviceId) {
        UserDocument user = securityUtils.getCurrentUserDocument();
        userProfileService.removeDevice(user.getId(), deviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<UserSessionDocument>> getActiveSessions() {
        UserDocument user = securityUtils.getCurrentUserDocument();
        return ResponseEntity.ok(userProfileService.getActiveSessions(user.getId()));
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> terminateSession(@PathVariable UUID sessionId) {
        UserDocument user = securityUtils.getCurrentUserDocument();
        userProfileService.terminateSession(user.getId(), sessionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sessions/others")
    public ResponseEntity<Void> terminateAllOtherSessions() {
        UserDocument user = securityUtils.getCurrentUserDocument();
        String currentSessionToken = securityUtils.getCurrentSessionToken();
        userProfileService.terminateAllOtherSessions(user.getId(), currentSessionToken);
        return ResponseEntity.ok().build();
    }
}
