package com.eyenet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowRuleDTO {
    private UUID id;
    private UUID deviceId;
    private String name;
    private String description;
    private Integer tableId;
    private Integer priority;
    private Long cookie;
    private String matchCriteria;
    private String actions;
    private Integer idleTimeout;
    private Integer hardTimeout;
    private String status;
    private UUID departmentId;
    private Long bytesCount;
    private Long packetsCount;
    private LocalDateTime lastMatched;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
}
