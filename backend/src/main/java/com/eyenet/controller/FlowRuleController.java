package com.eyenet.controller;

import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.FlowRule;
import com.eyenet.model.entity.FlowRuleTemplate;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.service.DepartmentService;
import com.eyenet.service.FlowRuleService;
import com.eyenet.service.NetworkDeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flow-rules")
@RequiredArgsConstructor
public class FlowRuleController {
    private final FlowRuleService flowRuleService;
    private final DepartmentService departmentService;
    private final NetworkDeviceService deviceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlowRule> createFlowRule(@Valid @RequestBody FlowRule rule) {
        return ResponseEntity.ok(flowRuleService.createFlowRule(rule));
    }

    @PutMapping("/{ruleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlowRule> updateFlowRule(
            @PathVariable UUID ruleId,
            @Valid @RequestBody FlowRule rule) {
        return ResponseEntity.ok(flowRuleService.updateFlowRule(ruleId, rule));
    }

    @DeleteMapping("/{ruleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFlowRule(@PathVariable UUID ruleId) {
        flowRuleService.deleteFlowRule(ruleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Page<FlowRule>> getFlowRulesByDepartment(
            @PathVariable UUID departmentId,
            Pageable pageable) {
        Department department = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(flowRuleService.getFlowRulesByDepartment(department, pageable));
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<FlowRule>> getFlowRulesByDevice(
            @PathVariable UUID deviceId) {
        NetworkDevice device = deviceService.getDevice(deviceId);
        return ResponseEntity.ok(flowRuleService.getFlowRulesByDevice(device));
    }

    @PostMapping("/templates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlowRuleTemplate> createTemplate(
            @Valid @RequestBody FlowRuleTemplate template) {
        return ResponseEntity.ok(flowRuleService.createTemplate(template));
    }

    @PostMapping("/templates/{templateId}/apply")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlowRule> createRuleFromTemplate(
            @PathVariable UUID templateId,
            @RequestParam UUID deviceId) {
        NetworkDevice device = deviceService.getDevice(deviceId);
        return ResponseEntity.ok(flowRuleService.createRuleFromTemplate(templateId, device));
    }
}
