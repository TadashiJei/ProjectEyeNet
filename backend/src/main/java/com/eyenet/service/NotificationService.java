package com.eyenet.service;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.model.document.UserNotificationDocument;
import com.eyenet.repository.mongodb.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserNotificationRepository notificationRepository;

    public void sendAlert(AlertDocument alert) {
        if (alert.getSeverity() == AlertDocument.Severity.HIGH) {
            sendUrgentNotification(alert);
        } else {
            sendStandardNotification(alert);
        }
    }

    public void sendUrgentNotification(AlertDocument alert) {
        LocalDateTime now = LocalDateTime.now();
        UserNotificationDocument notification = UserNotificationDocument.builder()
                .userId(alert.getUserId())
                .title("Urgent Alert: " + alert.getTitle())
                .message(alert.getMessage())
                .type("ALERT")
                .priority("HIGH")
                .read(false)
                .createdAt(now)
                .timestamp(now)
                .build();
        notificationRepository.save(notification);
    }

    public void sendStandardNotification(AlertDocument alert) {
        LocalDateTime now = LocalDateTime.now();
        UserNotificationDocument notification = UserNotificationDocument.builder()
                .userId(alert.getUserId())
                .title(alert.getTitle())
                .message(alert.getMessage())
                .type("ALERT")
                .priority("NORMAL")
                .read(false)
                .createdAt(now)
                .timestamp(now)
                .build();
        notificationRepository.save(notification);
    }
}
