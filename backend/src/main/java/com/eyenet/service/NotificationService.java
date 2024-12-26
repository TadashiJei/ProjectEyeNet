package com.eyenet.service;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.model.document.UserNotificationDocument;
import com.eyenet.repository.mongodb.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
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
                .userId(alert.getTargetUserId())
                .title("Urgent Alert: " + alert.getAlertTitle())
                .message(alert.getDescription())
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
                .userId(alert.getTargetUserId())
                .title(alert.getAlertTitle())
                .message(alert.getDescription())
                .type("ALERT")
                .priority("NORMAL")
                .read(false)
                .createdAt(now)
                .timestamp(now)
                .build();
        notificationRepository.save(notification);
    }
}
