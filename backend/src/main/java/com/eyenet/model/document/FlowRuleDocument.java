package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flow_rules")
public class FlowRuleDocument {
    public enum FlowRuleStatus {
        ACTIVE,
        INACTIVE,
        EXPIRED,
        PENDING,
        ERROR
    }

    @Id
    private UUID id;

    @DBRef
    @Field("device")
    private NetworkDeviceDocument device;

    @Field("priority")
    private int priority;

    @Field("match_criteria")
    private String matchCriteria;

    @Field("actions")
    private List<String> actions;

    @Field("idle_timeout")
    private int idleTimeout;

    @Field("hard_timeout")
    private int hardTimeout;

    @Field("status")
    private FlowRuleStatus status;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("last_matched")
    private LocalDateTime lastMatched;

    @Field("department_id")
    private UUID departmentId;

    @Field("error_message")
    private String errorMessage;
}
