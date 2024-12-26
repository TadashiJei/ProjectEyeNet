package com.eyenet.service;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.repository.mongodb.FlowRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FlowRuleServiceTest {
    @Mock
    private FlowRuleRepository flowRuleRepository;

    @InjectMocks
    private FlowRuleService flowRuleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFlowRule() {
        FlowRuleDocument flowRule = FlowRuleDocument.builder()
                .name("Test Flow Rule")
                .description("Test Description")
                .priority(1)
                .matchFields(new HashMap<>())
                .actions(new ArrayList<>())
                .status(FlowRuleDocument.FlowRuleStatus.ACTIVE)
                .build();

        when(flowRuleRepository.save(any(FlowRuleDocument.class))).thenReturn(flowRule);

        FlowRuleDocument savedRule = flowRuleService.createFlowRule(flowRule);

        assertNotNull(savedRule);
        assertEquals("Test Flow Rule", savedRule.getName());
        verify(flowRuleRepository, times(1)).save(any(FlowRuleDocument.class));
    }

    @Test
    void testUpdateFlowRule() {
        UUID ruleId = UUID.randomUUID();
        FlowRuleDocument existingRule = FlowRuleDocument.builder()
                .id(ruleId)
                .name("Original Rule")
                .build();

        FlowRuleDocument updatedRule = FlowRuleDocument.builder()
                .id(ruleId)
                .name("Updated Rule")
                .build();

        when(flowRuleRepository.findById(ruleId)).thenReturn(Optional.of(existingRule));
        when(flowRuleRepository.save(any(FlowRuleDocument.class))).thenReturn(updatedRule);

        FlowRuleDocument result = flowRuleService.updateFlowRule(ruleId, updatedRule);

        assertNotNull(result);
        assertEquals("Updated Rule", result.getName());
        verify(flowRuleRepository, times(1)).findById(ruleId);
        verify(flowRuleRepository, times(1)).save(any(FlowRuleDocument.class));
    }

    @Test
    void testDeleteFlowRule() {
        UUID ruleId = UUID.randomUUID();
        FlowRuleDocument existingRule = FlowRuleDocument.builder()
                .id(ruleId)
                .name("Rule to Delete")
                .build();

        when(flowRuleRepository.findById(ruleId)).thenReturn(Optional.of(existingRule));
        doNothing().when(flowRuleRepository).deleteById(ruleId);

        flowRuleService.deleteFlowRule(ruleId);

        verify(flowRuleRepository, times(1)).findById(ruleId);
        verify(flowRuleRepository, times(1)).deleteById(ruleId);
    }

    @Test
    void testGetFlowRulesByDepartment() {
        UUID departmentId = UUID.randomUUID();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<FlowRuleDocument> rules = new ArrayList<>();
        rules.add(FlowRuleDocument.builder().name("Rule 1").build());
        rules.add(FlowRuleDocument.builder().name("Rule 2").build());
        Page<FlowRuleDocument> rulePage = new PageImpl<>(rules, pageRequest, rules.size());

        when(flowRuleRepository.findByDepartmentId(eq(departmentId), any(PageRequest.class)))
                .thenReturn(rulePage);

        Page<FlowRuleDocument> result = flowRuleService.getFlowRulesByDepartment(departmentId, pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(flowRuleRepository, times(1))
                .findByDepartmentId(eq(departmentId), any(PageRequest.class));
    }
}
