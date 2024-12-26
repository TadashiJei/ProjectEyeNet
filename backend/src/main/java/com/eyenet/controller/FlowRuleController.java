package com.eyenet.controller;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.entity.Department;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.service.DepartmentService;
import com.eyenet.service.FlowRuleService;
import com.eyenet.service.NetworkDeviceService;
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
@RequestMapping("/api/flow-rules")
@RequiredArgsConstructor
public class FlowRuleController {
    private final FlowRuleService flowRuleService;
    private final DepartmentService departmentService;
    private final NetworkDeviceService networkDeviceService;

    @PostMapping
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<FlowRuleDocument> createFlowRule(@Valid @RequestBody FlowRuleDocument flowRule) {
        return ResponseEntity.ok(flowRuleService.createRule(flowRule));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<FlowRuleDocument> updateFlowRule(
            @PathVariable UUID id,
            @Valid @RequestBody FlowRuleDocument flowRule) {
        return ResponseEntity.ok(flowRuleService.updateRule(id, flowRule));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<Void> deleteFlowRule(@PathVariable UUID id) {
        flowRuleService.deleteRule(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<Page<FlowRuleDocument>> getFlowRulesByDepartment(
            @PathVariable UUID departmentId,
            Pageable pageable) {
        Department department = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(flowRuleService.getFlowRulesByDepartment(departmentId, pageable));
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<List<FlowRuleDocument>> getFlowRulesByDevice(@PathVariable UUID deviceId) {
        NetworkDevice device = networkDeviceService.getDeviceById(deviceId);
        return ResponseEntity.ok(flowRuleService.getRulesByDevice(deviceId));
    }

    @PostMapping("/device/{deviceId}/template")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<FlowRuleDocument> createFromTemplate(@PathVariable UUID deviceId) {
        NetworkDevice device = networkDeviceService.getDeviceById(deviceId);
        return ResponseEntity.ok(flowRuleService.createRuleFromTemplate(deviceId, device));
    }
}
