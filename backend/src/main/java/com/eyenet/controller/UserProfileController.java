package com.eyenet.controller;

import com.eyenet.model.entity.*;
import com.eyenet.model.dto.*;
import com.eyenet.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile() {
        return ResponseEntity.ok(userProfileService.getCurrentUserProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(userProfileService.updateProfile(request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        userProfileService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/activity")
    public ResponseEntity<List<UserActivity>> getUserActivity() {
        return ResponseEntity.ok(userProfileService.getCurrentUserActivity());
    }

    @GetMapping("/me/network-usage")
    public ResponseEntity<UserNetworkUsage> getUserNetworkUsage() {
        return ResponseEntity.ok(userProfileService.getCurrentUserNetworkUsage());
    }

    @GetMapping("/me/preferences")
    public ResponseEntity<UserPreferences> getUserPreferences() {
        return ResponseEntity.ok(userProfileService.getCurrentUserPreferences());
    }

    @PutMapping("/me/preferences")
    public ResponseEntity<UserPreferences> updatePreferences(@Valid @RequestBody UserPreferences preferences) {
        return ResponseEntity.ok(userProfileService.updatePreferences(preferences));
    }

    @GetMapping("/me/notifications")
    public ResponseEntity<List<UserNotification>> getUserNotifications() {
        return ResponseEntity.ok(userProfileService.getCurrentUserNotifications());
    }

    @PutMapping("/me/notifications/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable UUID notificationId) {
        userProfileService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/notifications/read-all")
    public ResponseEntity<Void> markAllNotificationsAsRead() {
        userProfileService.markAllNotificationsAsRead();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/devices")
    public ResponseEntity<List<UserDevice>> getUserDevices() {
        return ResponseEntity.ok(userProfileService.getCurrentUserDevices());
    }

    @PostMapping("/me/devices")
    public ResponseEntity<UserDevice> registerDevice(@Valid @RequestBody UserDeviceRegistration request) {
        return ResponseEntity.ok(userProfileService.registerDevice(request));
    }

    @DeleteMapping("/me/devices/{deviceId}")
    public ResponseEntity<Void> removeDevice(@PathVariable UUID deviceId) {
        userProfileService.removeDevice(deviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/sessions")
    public ResponseEntity<List<UserSession>> getUserSessions() {
        return ResponseEntity.ok(userProfileService.getCurrentUserSessions());
    }

    @DeleteMapping("/me/sessions/{sessionId}")
    public ResponseEntity<Void> terminateSession(@PathVariable UUID sessionId) {
        userProfileService.terminateSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/sessions")
    public ResponseEntity<Void> terminateAllOtherSessions() {
        userProfileService.terminateAllOtherSessions();
        return ResponseEntity.ok().build();
    }
}
