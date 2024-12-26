package com.eyenet.controller;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.model.document.AlertRuleDocument;
import com.eyenet.model.entity.Department;
import com.eyenet.service.AlertService;
import com.eyenet.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;
    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<Page<AlertDocument>> getAllAlerts(Pageable pageable) {
        return ResponseEntity.ok(alertService.getAllAlerts(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<AlertDocument> getAlert(@PathVariable UUID id) {
        return ResponseEntity.ok(alertService.getAlert(id));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<Page<AlertDocument>> getAlertsByDepartment(
            @PathVariable UUID departmentId,
            Pageable pageable) {
        Department department = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(alertService.getAlertsByDepartment(departmentId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<AlertDocument> createAlert(@Valid @RequestBody AlertDocument alert) {
        return ResponseEntity.ok(alertService.createAlert(alert));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<AlertDocument> updateAlert(
            @PathVariable UUID id,
            @Valid @RequestBody AlertDocument alert) {
        return ResponseEntity.ok(alertService.updateAlert(id, alert));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<Void> deleteAlert(@PathVariable UUID id) {
        alertService.deleteAlert(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rules")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<AlertRuleDocument> createRule(@Valid @RequestBody AlertRuleDocument rule) {
        return ResponseEntity.ok(alertService.createRule(rule));
    }

    @PutMapping("/rules/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<AlertRuleDocument> updateRule(
            @PathVariable UUID id,
            @Valid @RequestBody AlertRuleDocument rule) {
        return ResponseEntity.ok(alertService.updateRule(id, rule));
    }

    @GetMapping("/rules/department/{departmentId}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<List<AlertRuleDocument>> getRulesByDepartment(@PathVariable UUID departmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(alertService.getRulesByDepartment(departmentId));
    }
}
