package com.eyenet.service;

import com.eyenet.model.document.DepartmentDocument;
import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.document.FlowRuleTemplateDocument;
import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.repository.mongodb.FlowRuleRepository;
import com.eyenet.repository.mongodb.FlowRuleTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlowRuleService {
    private final FlowRuleRepository flowRuleRepository;
    private final FlowRuleTemplateRepository flowRuleTemplateRepository;
    private final NetworkDeviceService deviceService;
    private final OpenFlowService openFlowService;

    @Transactional
    public FlowRuleDocument createFlowRule(FlowRuleDocument rule) {
        validateFlowRule(rule);
        
        // Set initial status
        rule.setStatus(FlowRuleDocument.FlowRuleStatus.PENDING);
        FlowRuleDocument savedRule = flowRuleRepository.save(rule);
        
        // Apply rule to device
        try {
            openFlowService.applyFlowRule(rule);
            savedRule.setStatus(FlowRuleDocument.FlowRuleStatus.ACTIVE);
        } catch (Exception e) {
            savedRule.setStatus(FlowRuleDocument.FlowRuleStatus.ERROR);
            // Log error and potentially notify administrators
        }
        
        return flowRuleRepository.save(savedRule);
    }

    @Transactional
    public FlowRuleDocument updateFlowRule(UUID ruleId, FlowRuleDocument updatedRule) {
        FlowRuleDocument existingRule = flowRuleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("Flow rule not found"));
        
        validateFlowRule(updatedRule);
        
        // Remove old rule from device
        try {
            openFlowService.removeFlowRule(existingRule);
        } catch (Exception e) {
            // Log error but continue with update
        }
        
        // Update fields
        existingRule.setName(updatedRule.getName());
        existingRule.setDescription(updatedRule.getDescription());
        existingRule.setPriority(updatedRule.getPriority());
        existingRule.setMatchCriteria(updatedRule.getMatchCriteria());
        existingRule.setActions(updatedRule.getActions());
        existingRule.setIdleTimeout(updatedRule.getIdleTimeout());
        existingRule.setHardTimeout(updatedRule.getHardTimeout());
        existingRule.setStatus(FlowRuleDocument.FlowRuleStatus.PENDING);
        
        FlowRuleDocument savedRule = flowRuleRepository.save(existingRule);
        
        // Apply updated rule to device
        try {
            openFlowService.applyFlowRule(savedRule);
            savedRule.setStatus(FlowRuleDocument.FlowRuleStatus.ACTIVE);
        } catch (Exception e) {
            savedRule.setStatus(FlowRuleDocument.FlowRuleStatus.ERROR);
            // Log error and potentially notify administrators
        }
        
        return flowRuleRepository.save(savedRule);
    }

    @Transactional
    public void deleteFlowRule(UUID ruleId) {
        FlowRuleDocument rule = flowRuleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("Flow rule not found"));
        
        try {
            openFlowService.removeFlowRule(rule);
            rule.setStatus(FlowRuleDocument.FlowRuleStatus.DELETED);
            flowRuleRepository.save(rule);
        } catch (Exception e) {
            // Log error but continue with deletion
            flowRuleRepository.delete(rule);
        }
    }

    @Transactional(readOnly = true)
    public Page<FlowRuleDocument> getFlowRulesByDepartment(DepartmentDocument department, Pageable pageable) {
        return flowRuleRepository.findByDepartmentId(department.getId(), pageable);
    }

    @Transactional(readOnly = true)
    public List<FlowRuleDocument> getFlowRulesByDevice(NetworkDeviceDocument device) {
        return flowRuleRepository.findByDevice(device);
    }

    @Transactional
    public FlowRuleTemplateDocument createTemplate(FlowRuleTemplateDocument template) {
        validateTemplate(template);
        return flowRuleTemplateRepository.save(template);
    }

    @Transactional
    public FlowRuleDocument createRuleFromTemplate(UUID templateId, NetworkDeviceDocument device) {
        FlowRuleTemplateDocument template = flowRuleTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        
        FlowRuleDocument rule = FlowRuleDocument.builder()
                .name(template.getName() + " - " + device.getName())
                .description(template.getDescription())
                .device(device)
                .priority(template.getPriority())
                .matchCriteria(template.getMatchCriteria())
                .actions(template.getActions())
                .idleTimeout(template.getIdleTimeout())
                .hardTimeout(template.getHardTimeout())
                .departmentId(template.getDepartmentId())
                .build();
        
        return createFlowRule(rule);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void cleanupExpiredRules() {
        List<FlowRuleDocument> expiredRules = flowRuleRepository.findExpiredRules(LocalDateTime.now());
        for (FlowRuleDocument rule : expiredRules) {
            try {
                openFlowService.removeFlowRule(rule);
                rule.setStatus(FlowRuleDocument.FlowRuleStatus.INACTIVE);
                flowRuleRepository.save(rule);
            } catch (Exception e) {
                // Log error but continue with cleanup
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void cleanupStaleRules() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        List<FlowRuleDocument> staleRules = flowRuleRepository.findStaleRules(threshold);
        for (FlowRuleDocument rule : staleRules) {
            try {
                openFlowService.removeFlowRule(rule);
                rule.setStatus(FlowRuleDocument.FlowRuleStatus.INACTIVE);
                flowRuleRepository.save(rule);
            } catch (Exception e) {
                // Log error but continue with cleanup
            }
        }
    }

    private void validateFlowRule(FlowRuleDocument rule) {
        if (rule.getPriority() == null || rule.getPriority() < 0) {
            throw new IllegalArgumentException("Invalid priority");
        }
        
        if (rule.getDevice() == null) {
            throw new IllegalArgumentException("Device is required");
        }
        
        // Validate match criteria and actions format
        // This would depend on your OpenFlow implementation
        if (rule.getMatchCriteria() == null || rule.getMatchCriteria().isEmpty()) {
            throw new IllegalArgumentException("Match criteria is required");
        }
        
        if (rule.getActions() == null || rule.getActions().isEmpty()) {
            throw new IllegalArgumentException("Actions are required");
        }
    }

    private void validateTemplate(FlowRuleTemplateDocument template) {
        if (template.getPriority() != null && template.getPriority() < 0) {
            throw new IllegalArgumentException("Invalid priority");
        }
        
        if (flowRuleTemplateRepository.existsByNameAndDepartmentId(template.getName(), 
                template.getDepartmentId())) {
            throw new IllegalArgumentException(
                    "Template with this name already exists for the department");
        }
        
        // Validate match criteria and actions format
        if (template.getMatchCriteria() == null || template.getMatchCriteria().isEmpty()) {
            throw new IllegalArgumentException("Match criteria is required");
        }
        
        if (template.getActions() == null || template.getActions().isEmpty()) {
            throw new IllegalArgumentException("Actions are required");
        }
    }
}
