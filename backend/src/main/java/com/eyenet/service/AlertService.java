package com.eyenet.service;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.model.document.AlertRuleDocument;
import com.eyenet.repository.AlertDocumentRepository;
import com.eyenet.repository.AlertRuleDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertDocumentRepository alertRepository;
    private final AlertRuleDocumentRepository alertRuleRepository;
    private final NotificationService notificationService;

    // Cache to prevent alert flooding
    private final Map<String, LocalDateTime> lastAlertTimes = new ConcurrentHashMap<>();

    @Transactional
    public AlertDocument createAlert(AlertDocument alert) {
        String cacheKey = generateCacheKey(alert);
        LocalDateTime lastAlertTime = lastAlertTimes.get(cacheKey);
        
        if (lastAlertTime != null) {
            AlertRuleDocument rule = alertRuleRepository.findById(alert.getId())
                    .orElse(null);
            
            if (rule != null && rule.getParameters() != null && 
                rule.getParameters().containsKey("cooldownMinutes")) {
                Integer cooldownMinutes = (Integer) rule.getParameters().get("cooldownMinutes");
                LocalDateTime cooldownEnd = lastAlertTime.plusMinutes(cooldownMinutes);
                if (LocalDateTime.now().isBefore(cooldownEnd)) {
                    return null; // Skip alert during cooldown
                }
            }
        }
        
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        AlertDocument savedAlert = alertRepository.save(alert);
        lastAlertTimes.put(cacheKey, LocalDateTime.now());
        
        // Send notifications based on severity
        if (savedAlert.getSeverity() == AlertDocument.Severity.HIGH || 
            savedAlert.getSeverity() == AlertDocument.Severity.CRITICAL) {
            notificationService.sendUrgentNotification(savedAlert);
        } else {
            notificationService.sendStandardNotification(savedAlert);
        }
        
        return savedAlert;
    }

    @Transactional
    public AlertRuleDocument createAlertRule(AlertRuleDocument rule) {
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        return alertRuleRepository.save(rule);
    }

    @Transactional
    public AlertDocument updateAlertStatus(UUID alertId, AlertDocument.AlertStatus newStatus) {
        AlertDocument alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        
        alert.setStatus(newStatus);
        alert.setUpdatedAt(LocalDateTime.now());
        
        return alertRepository.save(alert);
    }

    @Transactional
    public AlertDocument assignAlert(UUID alertId, UUID userId) {
        AlertDocument alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        
        alert.setAssignedTo(userId);
        alert.setUpdatedAt(LocalDateTime.now());
        
        return alertRepository.save(alert);
    }

    public Page<AlertDocument> getAlertsByDepartment(UUID departmentId, Pageable pageable) {
        return alertRepository.findByDepartmentId(departmentId, pageable);
    }

    public List<AlertDocument> getActiveAlerts(UUID departmentId) {
        return alertRepository.findByDepartmentIdAndStatus(departmentId, AlertDocument.AlertStatus.NEW);
    }

    public List<AlertRuleDocument> getAlertRules(UUID departmentId) {
        return alertRuleRepository.findByDepartmentId(departmentId);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void cleanupAlertCache() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        lastAlertTimes.entrySet().removeIf(entry -> entry.getValue().isBefore(threshold));
    }

    private String generateCacheKey(AlertDocument alert) {
        return String.format("%s:%s:%s",
                alert.getDepartmentId(),
                alert.getDeviceId(),
                alert.getSeverity());
    }
}
