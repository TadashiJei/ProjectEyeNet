package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flow_rule_templates")
public class FlowRuleTemplateDocument {
    @Id
    private UUID id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("priority")
    private int priority;

    @Field("match_fields")
    private Map<String, String> matchFields;

    @Field("actions")
    private List<String> actions;

    @Field("timeout_idle")
    private Integer timeoutIdle;

    @Field("timeout_hard")
    private Integer timeoutHard;

    @Field("department_id")
    private UUID departmentId;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("created_by")
    private UUID createdBy;

    @Field("updated_by")
    private UUID updatedBy;

    @Field("is_active")
    private boolean isActive;

    @Field("version")
    private int version;

    @Field("metadata")
    private Map<String, String> metadata;
}
