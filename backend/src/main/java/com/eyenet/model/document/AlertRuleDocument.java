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
@Document(collection = "alert_rules")
public class AlertRuleDocument {
    public enum RuleType {
        THRESHOLD, PATTERN, ANOMALY, COMPOSITE
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Action {
        NOTIFY, ESCALATE, AUTO_RESOLVE, TRIGGER_WORKFLOW
    }

    @Id
    private UUID id;
    private String name;
    private String description;
    private RuleType type;
    private Severity severity;
    private String condition;
    private Map<String, Object> parameters;
    private boolean enabled;
    private UUID departmentId;
    private String notificationChannel;
    private String notificationTemplate;
    private double threshold;
    private Action action;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
