package com.eyenet.controller;

import com.eyenet.model.entity.Alert;
import com.eyenet.model.entity.AlertRule;
import com.eyenet.model.entity.Department;
import com.eyenet.service.AlertService;
import com.eyenet.service.DepartmentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;
    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Alert> createAlert(@Valid @RequestBody Alert alert) {
        return ResponseEntity.ok(alertService.createAlert(alert));
    }

    @PutMapping("/{alertId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Alert> updateAlertStatus(
            @PathVariable UUID alertId,
            @RequestParam Alert.AlertStatus status,
            @RequestParam(required = false) UUID resolvedBy,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(alertService.updateAlertStatus(alertId, status, resolvedBy, notes));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Page<Alert>> getAlertsByDepartment(
            @PathVariable UUID departmentId,
            Pageable pageable) {
        Department department = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(alertService.getAlertsByDepartment(department, pageable));
    }

    @GetMapping("/high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<Alert>> getActiveHighPriorityAlerts() {
        return ResponseEntity.ok(alertService.getActiveHighPriorityAlerts());
    }

    @GetMapping("/unresolved")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<Alert>> getUnresolvedAlerts(
            @RequestParam Alert.Severity severity) {
        return ResponseEntity.ok(alertService.getUnresolvedAlerts(severity));
    }

    @PostMapping("/rules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertRule> createAlertRule(@Valid @RequestBody AlertRule rule) {
        return ResponseEntity.ok(alertService.createAlertRule(rule));
    }

    @PutMapping("/rules/{ruleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertRule> updateAlertRule(
            @PathVariable UUID ruleId,
            @Valid @RequestBody AlertRule rule) {
        return ResponseEntity.ok(alertService.updateAlertRule(ruleId, rule));
    }

    @GetMapping("/rules/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<AlertRule>> getActiveRulesForDepartment(
            @PathVariable UUID departmentId) {
        Department department = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(alertService.getActiveRulesForDepartment(department));
    }
}
