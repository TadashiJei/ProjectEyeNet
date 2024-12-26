package com.eyenet.service;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.model.document.AlertRuleDocument;
import com.eyenet.repository.mongodb.AlertRepository;
import com.eyenet.repository.mongodb.AlertRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {
    private final AlertRepository alertRepository;
    private final AlertRuleRepository alertRuleRepository;
    private final NotificationService notificationService;

    public Page<AlertDocument> getAllAlerts(Pageable pageable) {
        return alertRepository.findAll(pageable);
    }

    public AlertDocument getAlert(UUID id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alert not found with id: " + id));
    }

    public Page<AlertDocument> getAlertsByDepartment(UUID departmentId, Pageable pageable) {
        return alertRepository.findByDepartmentId(departmentId, pageable);
    }

    public AlertDocument createAlert(AlertDocument alert) {
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        AlertDocument savedAlert = alertRepository.save(alert);
        notificationService.sendAlertNotification(savedAlert);
        return savedAlert;
    }

    public AlertDocument updateAlert(UUID id, AlertDocument alert) {
        AlertDocument existingAlert = getAlert(id);
        alert.setId(id);
        alert.setCreatedAt(existingAlert.getCreatedAt());
        alert.setUpdatedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    public void deleteAlert(UUID id) {
        alertRepository.deleteById(id);
    }

    public AlertRuleDocument createRule(AlertRuleDocument rule) {
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        return alertRuleRepository.save(rule);
    }

    public AlertRuleDocument updateRule(UUID id, AlertRuleDocument rule) {
        if (!alertRuleRepository.existsById(id)) {
            throw new EntityNotFoundException("Alert rule not found with id: " + id);
        }
        rule.setId(id);
        rule.setUpdatedAt(LocalDateTime.now());
        return alertRuleRepository.save(rule);
    }

    public List<AlertRuleDocument> getRulesByDepartment(UUID departmentId) {
        return alertRuleRepository.findByDepartmentId(departmentId);
    }
}
