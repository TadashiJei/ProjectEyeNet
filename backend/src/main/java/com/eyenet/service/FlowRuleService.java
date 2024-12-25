package com.eyenet.service;

import com.eyenet.model.entity.*;
import com.eyenet.repository.jpa.FlowRuleRepository;
import com.eyenet.repository.jpa.FlowRuleTemplateRepository;
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
    private final FlowRuleTemplateRepository templateRepository;
    private final NetworkDeviceService deviceService;
    private final OpenFlowService openFlowService;

    @Transactional
    public FlowRule createFlowRule(FlowRule rule) {
        validateFlowRule(rule);
        
        // Set initial status
        rule.setStatus(FlowRule.FlowRuleStatus.PENDING);
        FlowRule savedRule = flowRuleRepository.save(rule);
        
        // Apply rule to device
        try {
            openFlowService.applyFlowRule(rule);
            savedRule.setStatus(FlowRule.FlowRuleStatus.ACTIVE);
        } catch (Exception e) {
            savedRule.setStatus(FlowRule.FlowRuleStatus.ERROR);
            // Log error and potentially notify administrators
        }
        
        return flowRuleRepository.save(savedRule);
    }

    @Transactional
    public FlowRule updateFlowRule(UUID ruleId, FlowRule updatedRule) {
        FlowRule existingRule = flowRuleRepository.findById(ruleId)
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
        existingRule.setStatus(FlowRule.FlowRuleStatus.PENDING);
        
        FlowRule savedRule = flowRuleRepository.save(existingRule);
        
        // Apply updated rule to device
        try {
            openFlowService.applyFlowRule(savedRule);
            savedRule.setStatus(FlowRule.FlowRuleStatus.ACTIVE);
        } catch (Exception e) {
            savedRule.setStatus(FlowRule.FlowRuleStatus.ERROR);
            // Log error and potentially notify administrators
        }
        
        return flowRuleRepository.save(savedRule);
    }

    @Transactional
    public void deleteFlowRule(UUID ruleId) {
        FlowRule rule = flowRuleRepository.findById(ruleId)
                .orElseThrow(() -> new IllegalArgumentException("Flow rule not found"));
        
        try {
            openFlowService.removeFlowRule(rule);
            rule.setStatus(FlowRule.FlowRuleStatus.DELETED);
            flowRuleRepository.save(rule);
        } catch (Exception e) {
            // Log error but continue with deletion
            flowRuleRepository.delete(rule);
        }
    }

    @Transactional(readOnly = true)
    public Page<FlowRule> getFlowRulesByDepartment(Department department, Pageable pageable) {
        return flowRuleRepository.findByDepartment(department, pageable);
    }

    @Transactional(readOnly = true)
    public List<FlowRule> getFlowRulesByDevice(NetworkDevice device) {
        return flowRuleRepository.findByDevice(device);
    }

    @Transactional
    public FlowRuleTemplate createTemplate(FlowRuleTemplate template) {
        validateTemplate(template);
        return templateRepository.save(template);
    }

    @Transactional
    public FlowRule createRuleFromTemplate(UUID templateId, NetworkDevice device) {
        FlowRuleTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        
        FlowRule rule = FlowRule.builder()
                .name(template.getName() + " - " + device.getName())
                .description(template.getDescription())
                .device(device)
                .priority(template.getPriority())
                .matchCriteria(template.getMatchCriteria())
                .actions(template.getActions())
                .idleTimeout(template.getIdleTimeout())
                .hardTimeout(template.getHardTimeout())
                .department(template.getDepartment())
                .build();
        
        return createFlowRule(rule);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void cleanupExpiredRules() {
        List<FlowRule> expiredRules = flowRuleRepository.findExpiredRules(LocalDateTime.now());
        for (FlowRule rule : expiredRules) {
            try {
                openFlowService.removeFlowRule(rule);
                rule.setStatus(FlowRule.FlowRuleStatus.INACTIVE);
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
        List<FlowRule> staleRules = flowRuleRepository.findStaleRules(threshold);
        for (FlowRule rule : staleRules) {
            try {
                openFlowService.removeFlowRule(rule);
                rule.setStatus(FlowRule.FlowRuleStatus.INACTIVE);
                flowRuleRepository.save(rule);
            } catch (Exception e) {
                // Log error but continue with cleanup
            }
        }
    }

    private void validateFlowRule(FlowRule rule) {
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

    private void validateTemplate(FlowRuleTemplate template) {
        if (template.getPriority() != null && template.getPriority() < 0) {
            throw new IllegalArgumentException("Invalid priority");
        }
        
        if (templateRepository.existsByNameAndDepartment(template.getName(), 
                template.getDepartment())) {
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
