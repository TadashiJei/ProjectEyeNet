package com.eyenet.service;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.repository.mongodb.FlowRuleRepository;
import com.eyenet.service.NetworkDeviceService;
import com.eyenet.service.OpenFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FlowRuleService {
    private final FlowRuleRepository flowRuleRepository;
    private final NetworkDeviceService networkDeviceService;
    private final OpenFlowService openFlowService;

    public FlowRuleDocument createRule(FlowRuleDocument flowRule) {
        // Validate device exists
        networkDeviceService.getDeviceById(flowRule.getDeviceId());
        
        FlowRuleDocument savedRule = flowRuleRepository.save(flowRule);
        
        // Apply the flow rule to the device
        openFlowService.applyFlowRule(savedRule);
        
        return savedRule;
    }

    public FlowRuleDocument updateRule(UUID id, FlowRuleDocument flowRule) {
        FlowRuleDocument existingRule = getRule(id);
        
        // Remove old flow rule from device
        openFlowService.removeFlowRule(existingRule);
        
        // Update and save the rule
        flowRule.setId(id);
        FlowRuleDocument updatedRule = flowRuleRepository.save(flowRule);
        
        // Apply updated rule to device
        openFlowService.applyFlowRule(updatedRule);
        
        return updatedRule;
    }

    public void deleteRule(UUID id) {
        FlowRuleDocument rule = getRule(id);
        openFlowService.removeFlowRule(rule);
        flowRuleRepository.deleteById(id);
    }

    public FlowRuleDocument getRule(UUID id) {
        return flowRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Flow rule not found with id: " + id));
    }

    public Page<FlowRuleDocument> getFlowRulesByDepartment(UUID departmentId, Pageable pageable) {
        return flowRuleRepository.findByDepartmentId(departmentId, pageable);
    }

    public List<FlowRuleDocument> getRulesByDevice(UUID deviceId) {
        return flowRuleRepository.findByDeviceId(deviceId);
    }

    public FlowRuleDocument createRuleFromTemplate(UUID deviceId, NetworkDevice device) {
        // Create a basic flow rule template for the device
        FlowRuleDocument template = FlowRuleDocument.builder()
                .deviceId(deviceId)
                .departmentId(device.getDepartment().getId())
                .priority(1000)
                .timeoutHard(0)
                .timeoutIdle(0)
                .build();
        
        return createRule(template);
    }
}
