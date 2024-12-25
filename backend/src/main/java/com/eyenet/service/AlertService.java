package com.eyenet.service;

import com.eyenet.model.entity.Alert;
import com.eyenet.model.entity.AlertRule;
import com.eyenet.model.entity.Department;
import com.eyenet.repository.jpa.AlertRepository;
import com.eyenet.repository.jpa.AlertRuleRepository;
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
    private final AlertRepository alertRepository;
    private final AlertRuleRepository alertRuleRepository;
    private final NotificationService notificationService;

    // Cache to prevent alert flooding
    private final Map<String, LocalDateTime> lastAlertTimes = new ConcurrentHashMap<>();

    @Transactional
    public Alert createAlert(Alert alert) {
        String cacheKey = generateCacheKey(alert);
        LocalDateTime lastAlertTime = lastAlertTimes.get(cacheKey);
        
        if (lastAlertTime != null) {
            AlertRule rule = alertRuleRepository.findById(alert.getId())
                    .orElse(null);
            
            if (rule != null && rule.getCooldownMinutes() != null) {
                LocalDateTime cooldownEnd = lastAlertTime.plusMinutes(rule.getCooldownMinutes());
                if (LocalDateTime.now().isBefore(cooldownEnd)) {
                    return null; // Skip alert during cooldown
                }
            }
        }
        
        Alert savedAlert = alertRepository.save(alert);
        lastAlertTimes.put(cacheKey, LocalDateTime.now());
        
        // Send notifications based on severity
        if (alert.getSeverity() == Alert.Severity.CRITICAL || 
            alert.getSeverity() == Alert.Severity.HIGH) {
            notificationService.sendHighPriorityAlert(alert);
        } else {
            notificationService.sendAlert(alert);
        }
        
        return savedAlert;
    }

    @Transactional
    public Alert updateAlertStatus(UUID alertId, Alert.AlertStatus newStatus, 
                                 UUID resolvedBy, String notes) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        
        alert.setStatus(newStatus);
        if (newStatus == Alert.AlertStatus.RESOLVED || 
            newStatus == Alert.AlertStatus.CLOSED) {
            alert.setResolvedAt(LocalDateTime.now());
            alert.setResolvedBy(resolvedBy);
            alert.setResolutionNotes(notes);
        }
        
        return alertRepository.save(alert);
    }

    @Transactional(readOnly = true)
    public Page<Alert> getAlertsByDepartment(Department department, Pageable pageable) {
        return alertRepository.findByDepartment(department, pageable);
    }

    @Transactional(readOnly = true)
    public List<Alert> getActiveHighPriorityAlerts() {
        return alertRepository.findActiveHighPriorityAlerts();
    }

    @Transactional(readOnly = true)
    public List<Alert> getUnresolvedAlerts(Alert.Severity severity) {
        return alertRepository.findUnresolvedAlertsBySeverity(severity);
    }

    @Transactional
    public AlertRule createAlertRule(AlertRule rule) {
        validateAlertRule(rule);
        return alertRuleRepository.save(rule);
    }

    @Transactional
    public AlertRule updateAlertRule(UUID ruleId, AlertRule updatedRule) {
        AlertRule existingRule = alertRuleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("Alert rule not found"));
        
        validateAlertRule(updatedRule);
        
        // Update fields
        existingRule.setName(updatedRule.getName());
        existingRule.setDescription(updatedRule.getDescription());
        existingRule.setAlertType(updatedRule.getAlertType());
        existingRule.setSeverity(updatedRule.getSeverity());
        existingRule.setDepartment(updatedRule.getDepartment());
        existingRule.setDeviceType(updatedRule.getDeviceType());
        existingRule.setMetricName(updatedRule.getMetricName());
        existingRule.setCondition(updatedRule.getCondition());
        existingRule.setThresholdValue(updatedRule.getThresholdValue());
        existingRule.setDurationMinutes(updatedRule.getDurationMinutes());
        existingRule.setCooldownMinutes(updatedRule.getCooldownMinutes());
        existingRule.setEnabled(updatedRule.isEnabled());
        
        return alertRuleRepository.save(existingRule);
    }

    @Transactional(readOnly = true)
    public List<AlertRule> getActiveRulesForDepartment(Department department) {
        return alertRuleRepository.findActiveRulesForDepartment(department);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void cleanupResolvedAlerts() {
        // Archive or delete old resolved alerts
        // Implementation depends on retention policy
    }

    private void validateAlertRule(AlertRule rule) {
        if (rule.getThresholdValue() == null) {
            throw new IllegalArgumentException("Threshold value is required");
        }
        
        if (rule.getDurationMinutes() != null && rule.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        
        if (rule.getCooldownMinutes() != null && rule.getCooldownMinutes() <= 0) {
            throw new IllegalArgumentException("Cooldown must be positive");
        }
        
        if (alertRuleRepository.existsByNameAndDepartment(rule.getName(), rule.getDepartment())) {
            throw new IllegalArgumentException("Rule with this name already exists for the department");
        }
    }

    private String generateCacheKey(Alert alert) {
        return String.format("%s:%s:%s", 
                alert.getType(), 
                alert.getSource(), 
                alert.getDeviceId() != null ? alert.getDeviceId() : "null");
    }
}
