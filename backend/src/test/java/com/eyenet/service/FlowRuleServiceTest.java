package com.eyenet.service;

import com.eyenet.model.entity.FlowRule;
import com.eyenet.repository.jpa.FlowRuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FlowRuleServiceTest {
    @Mock
    private FlowRuleRepository flowRuleRepository;

    @Mock
    private Channel channel;

    @Mock
    private ChannelId channelId;

    private Map<ChannelId, Channel> switchConnections;
    private FlowRuleService flowRuleService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        switchConnections = new HashMap<>();
        switchConnections.put(channelId, channel);
        objectMapper = new ObjectMapper();
        flowRuleService = new FlowRuleService(flowRuleRepository, switchConnections, objectMapper);

        when(channel.isActive()).thenReturn(true);
    }

    @Test
    void createFlowRule_Success() {
        // Prepare test data
        FlowRule flowRule = FlowRule.builder()
                .switchId("switch1")
                .tableId(0)
                .priority(100)
                .matchCriteria("{\"in_port\": 1}")
                .actions("{\"output\": 2}")
                .build();

        when(flowRuleRepository.save(any(FlowRule.class))).thenReturn(flowRule);

        // Execute test
        FlowRule result = flowRuleService.createFlowRule(flowRule);

        // Verify
        assertNotNull(result);
        assertEquals(flowRule.getSwitchId(), result.getSwitchId());
        assertEquals(flowRule.getTableId(), result.getTableId());
        verify(flowRuleRepository).save(any(FlowRule.class));
        verify(channel).writeAndFlush(any());
    }

    @Test
    void getFlowRulesBySwitchId_Success() {
        // Prepare test data
        String switchId = "switch1";
        List<FlowRule> expectedRules = List.of(
            FlowRule.builder().switchId(switchId).tableId(0).build(),
            FlowRule.builder().switchId(switchId).tableId(1).build()
        );

        when(flowRuleRepository.findBySwitchId(switchId)).thenReturn(expectedRules);

        // Execute test
        List<FlowRule> result = flowRuleService.getFlowRulesBySwitchId(switchId);

        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(flowRuleRepository).findBySwitchId(switchId);
    }

    @Test
    void deleteFlowRule_Success() {
        // Prepare test data
        String ruleId = "rule1";
        FlowRule flowRule = FlowRule.builder()
                .id(ruleId)
                .switchId("switch1")
                .tableId(0)
                .build();

        when(flowRuleRepository.findById(ruleId)).thenReturn(Optional.of(flowRule));

        // Execute test
        flowRuleService.deleteFlowRule(ruleId);

        // Verify
        verify(flowRuleRepository).deleteById(ruleId);
        verify(channel).writeAndFlush(any());
    }
}
