package com.eyenet.service;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.model.document.AlertRuleDocument;
import com.eyenet.repository.mongodb.AlertRepository;
import com.eyenet.repository.mongodb.AlertRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final AlertRuleRepository alertRuleRepository;
    private final NotificationService notificationService;

    public AlertDocument createAlert(AlertDocument alert) {
        alert.setCreatedAt(LocalDateTime.now());
        alert.setStatus(AlertDocument.AlertStatus.ACTIVE);
        
        AlertDocument savedAlert = alertRepository.save(alert);
        
        // Send notifications based on alert severity
        if (alert.getSeverity() == AlertDocument.Severity.HIGH) {
            notificationService.sendUrgentNotification(savedAlert);
        } else {
            notificationService.sendStandardNotification(savedAlert);
        }
        
        return savedAlert;
    }

    public AlertDocument updateAlertStatus(UUID alertId, AlertDocument.AlertStatus newStatus) {
        AlertDocument alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        
        alert.setStatus(newStatus);
        alert.setUpdatedAt(LocalDateTime.now());
        
        return alertRepository.save(alert);
    }

    public void deleteAlert(UUID alertId) {
        alertRepository.deleteById(alertId);
    }

    public AlertDocument getAlertById(UUID alertId) {
        return alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
    }

    public Page<AlertDocument> getAlertsByDepartment(UUID departmentId, Pageable pageable) {
        return alertRepository.findByDepartmentId(departmentId, pageable);
    }

    public List<AlertDocument> getActiveHighPriorityAlerts() {
        return alertRepository.findByStatusAndSeverity(AlertDocument.AlertStatus.ACTIVE, AlertDocument.Severity.HIGH);
    }

    public List<AlertDocument> getUnresolvedAlerts(AlertDocument.Severity severity) {
        return alertRepository.findByStatusAndSeverity(AlertDocument.AlertStatus.ACTIVE, severity);
    }

    public List<AlertDocument> getActiveAlertsByDepartment(UUID departmentId) {
        return alertRepository.findByDepartmentIdAndStatus(departmentId, AlertDocument.AlertStatus.ACTIVE);
    }

    public List<AlertRuleDocument> getDepartmentAlertRules(UUID departmentId) {
        return alertRuleRepository.findByDepartmentId(departmentId);
    }

    public AlertRuleDocument createAlertRule(AlertRuleDocument rule) {
        rule.setCreatedAt(LocalDateTime.now());
        return alertRuleRepository.save(rule);
    }

    public AlertRuleDocument updateAlertRule(UUID ruleId, AlertRuleDocument updatedRule) {
        AlertRuleDocument existingRule = alertRuleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("Alert rule not found"));
        
        existingRule.setCondition(updatedRule.getCondition());
        existingRule.setThreshold(updatedRule.getThreshold());
        existingRule.setAction(updatedRule.getAction());
        existingRule.setSeverity(updatedRule.getSeverity());
        existingRule.setUpdatedAt(LocalDateTime.now());
        
        return alertRuleRepository.save(existingRule);
    }

    public void deleteAlertRule(UUID ruleId) {
        alertRuleRepository.deleteById(ruleId);
    }

    public List<AlertRuleDocument> getActiveRulesForDepartment(UUID departmentId) {
        return alertRuleRepository.findByDepartmentIdAndEnabled(departmentId, true);
    }
}
