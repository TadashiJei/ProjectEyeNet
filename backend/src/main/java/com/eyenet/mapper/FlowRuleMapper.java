package com.eyenet.mapper;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.entity.FlowRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FlowRuleMapper {
    
    private final ObjectMapper objectMapper;
    
    public FlowRuleMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    public FlowRule mapToEntity(FlowRuleDocument document) {
        if (document == null) {
            return null;
        }
        
        try {
            return FlowRule.builder()
                    .id(document.getId())
                    .deviceId(document.getDeviceId())
                    .priority(document.getPriority())
                    .tableId(document.getTableId())
                    .matchCriteria(objectMapper.writeValueAsString(document.getMatchFields()))
                    .actions(objectMapper.writeValueAsString(document.getActions()))
                    .idleTimeout(document.getTimeoutIdle())
                    .hardTimeout(document.getTimeoutHard())
                    .cookie(document.getCookie())
                    .status(FlowRule.FlowRuleStatus.ACTIVE)
                    .createdAt(document.getCreatedAt())
                    .updatedAt(document.getUpdatedAt())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mapping FlowRuleDocument to FlowRule", e);
        }
    }
    
    public FlowRuleDocument mapToDocument(FlowRule entity) {
        if (entity == null) {
            return null;
        }
        
        try {
            TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
            return FlowRuleDocument.builder()
                    .id(entity.getId())
                    .deviceId(entity.getDeviceId())
                    .priority(entity.getPriority())
                    .tableId(entity.getTableId())
                    .matchFields(objectMapper.readValue(entity.getMatchCriteria(), typeRef))
                    .actions(objectMapper.readValue(entity.getActions(), typeRef))
                    .timeoutIdle(entity.getIdleTimeout())
                    .timeoutHard(entity.getHardTimeout())
                    .cookie(entity.getCookie())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mapping FlowRule to FlowRuleDocument", e);
        }
    }
}
