package com.eyenet.service;

import com.eyenet.model.entity.Alert;
import com.eyenet.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final UserService userService;

    public void sendAlert(Alert alert) {
        // Implementation for sending alerts
        // This could involve email, SMS, or in-app notifications
        if (alert.getSeverity() == Alert.Severity.HIGH) {
            sendHighPriorityAlert(alert);
        } else {
            sendNormalPriorityAlert(alert);
        }
    }

    public void sendHighPriorityAlert(Alert alert) {
        // Send high priority alert via all channels
        User user = userService.getUserById(alert.getUser().getId());
        String subject = "High Priority Alert: " + alert.getTitle();
        String content = alert.getMessage();
        
        sendEmail(user, subject, content);
        sendSMS(user, content);
        sendInAppNotification(user, content);
    }

    private void sendNormalPriorityAlert(Alert alert) {
        // Send normal priority alert via in-app notification only
        User user = userService.getUserById(alert.getUser().getId());
        sendInAppNotification(user, alert.getMessage());
    }

    public void sendEmail(User user, String subject, String content) {
        // Implementation for sending emails
    }

    public void sendSMS(User user, String message) {
        // Implementation for sending SMS
    }

    public void sendInAppNotification(User user, String message) {
        // Implementation for sending in-app notifications
    }
}
