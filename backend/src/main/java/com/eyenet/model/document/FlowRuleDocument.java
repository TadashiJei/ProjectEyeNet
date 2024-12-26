package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flow_rules")
public class FlowRuleDocument {
    @Id
    private UUID id;
    private UUID deviceId;
    private String name;
    private String description;
    private int priority;
    private int tableId;
    private Map<String, String> matchFields;
    private Map<String, String> actions;
    private int timeoutIdle;
    private int timeoutHard;
    private long cookie;
    private long bytesCount;
    private long packetsCount;
    private LocalDateTime lastMatched;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID departmentId;
}
