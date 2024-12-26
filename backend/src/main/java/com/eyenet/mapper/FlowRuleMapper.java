package com.eyenet.mapper;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.entity.FlowRule;
import org.springframework.stereotype.Component;

@Component
public class FlowRuleMapper {
    
    public FlowRule toEntity(FlowRuleDocument document) {
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
                .idleTimeout(document.getTimeoutIdle())
                .hardTimeout(document.getTimeoutHard())
                .cookie(document.getCookie())
                .status(FlowRule.FlowRuleStatus.ACTIVE)
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public FlowRuleDocument toDocument(FlowRule entity) {
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
                .timeoutIdle(entity.getIdleTimeout())
                .timeoutHard(entity.getHardTimeout())
                .cookie(entity.getCookie())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
