package com.eyenet.controller;

import com.eyenet.model.document.FlowRule;
import com.eyenet.service.FlowRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flow-rules")
public class FlowRuleController {
    private final FlowRuleService flowRuleService;

    public FlowRuleController(FlowRuleService flowRuleService) {
        this.flowRuleService = flowRuleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlowRule> createFlowRule(@RequestBody FlowRule flowRule) {
        return ResponseEntity.ok(flowRuleService.createFlowRule(flowRule));
    }

    @GetMapping("/switch/{switchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<FlowRule>> getFlowRulesBySwitchId(@PathVariable String switchId) {
        return ResponseEntity.ok(flowRuleService.getFlowRulesBySwitchId(switchId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFlowRule(@PathVariable String id) {
        flowRuleService.deleteFlowRule(id);
        return ResponseEntity.ok().build();
    }
}
