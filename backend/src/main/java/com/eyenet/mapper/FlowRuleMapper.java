package com.eyenet.mapper;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.entity.FlowRule;
import org.springframework.stereotype.Component;

@Component
public class FlowRuleMapper {
    
    public FlowRule mapToEntity(FlowRuleDocument document) {
        if (document == null) {
            return null;
        }
        
        return FlowRule.builder()
                .id(document.getId())
                .deviceId(document.getDeviceId())
                .priority(document.getPriority())
                .tableId(document.getTableId())
                .matchFields(document.getMatchFields())
                .actions(document.getActions())
                .timeoutIdle(document.getTimeoutIdle())
                .timeoutHard(document.getTimeoutHard())
                .cookie(document.getCookie())
                .flags(document.getFlags())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
    
    public FlowRuleDocument mapToDocument(FlowRule entity) {
        if (entity == null) {
            return null;
        }
        
        return FlowRuleDocument.builder()
                .id(entity.getId())
                .deviceId(entity.getDeviceId())
                .priority(entity.getPriority())
                .tableId(entity.getTableId())
                .matchFields(entity.getMatchFields())
                .actions(entity.getActions())
                .timeoutIdle(entity.getTimeoutIdle())
                .timeoutHard(entity.getTimeoutHard())
                .cookie(entity.getCookie())
                .flags(entity.getFlags())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
