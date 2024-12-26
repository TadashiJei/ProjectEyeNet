package com.eyenet.controller;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.model.document.AlertRuleDocument;
import com.eyenet.model.document.DepartmentDocument;
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
    public ResponseEntity<AlertDocument> createAlert(@Valid @RequestBody AlertDocument alert) {
        return ResponseEntity.ok(alertService.createAlert(alert));
    }

    @PutMapping("/{alertId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<AlertDocument> updateAlertStatus(
            @PathVariable UUID alertId,
            @RequestParam AlertDocument.AlertStatus status) {
        return ResponseEntity.ok(alertService.updateAlertStatus(alertId, status));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Page<AlertDocument>> getAlertsByDepartment(
            @PathVariable UUID departmentId,
            Pageable pageable) {
        DepartmentDocument department = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(alertService.getAlertsByDepartment(departmentId, pageable));
    }

    @GetMapping("/high-priority")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<AlertDocument>> getActiveHighPriorityAlerts() {
        return ResponseEntity.ok(alertService.getActiveHighPriorityAlerts());
    }

    @GetMapping("/unresolved")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<AlertDocument>> getUnresolvedAlerts(
            @RequestParam AlertDocument.Severity severity) {
        return ResponseEntity.ok(alertService.getUnresolvedAlerts(severity));
    }

    @PostMapping("/rules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertRuleDocument> createAlertRule(@Valid @RequestBody AlertRuleDocument rule) {
        return ResponseEntity.ok(alertService.createAlertRule(rule));
    }

    @PutMapping("/rules/{ruleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertRuleDocument> updateAlertRule(
            @PathVariable UUID ruleId,
            @Valid @RequestBody AlertRuleDocument rule) {
        return ResponseEntity.ok(alertService.updateAlertRule(ruleId, rule));
    }

    @GetMapping("/rules/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<AlertRuleDocument>> getActiveRulesForDepartment(
            @PathVariable UUID departmentId) {
        departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(alertService.getActiveRulesForDepartment(departmentId));
    }
}
