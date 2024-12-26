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

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("device_id")
    private UUID deviceId;

    @DBRef
    @Field("device")
    private NetworkDeviceDocument device;

    @Field("table_id")
    private Integer tableId;

    @Field("priority")
    private Integer priority;

    @Field("cookie")
    private Long cookie;

    @Field("idle_timeout")
    private Integer idleTimeout;

    @Field("hard_timeout")
    private Integer hardTimeout;

    @Field("match_fields")
    private Map<String, String> matchFields;

    @Field("actions")
    private Map<String, String> actions;

    @Field("flags")
    private Map<String, Boolean> flags;

    @Field("byte_count")
    private Long byteCount;

    @Field("packet_count")
    private Long packetCount;

    @Field("duration")
    private Long duration;

    @Field("status")
    private FlowRuleStatus status;

    @Field("enabled")
    private boolean enabled;

    @Field("metadata")
    private Map<String, String> metadata;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("created_by")
    private UUID createdBy;

    @Field("updated_by")
    private UUID updatedBy;

    public enum FlowRuleStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        ERROR,
        DELETED
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Integer getHardTimeout() {
        return hardTimeout;
    }

    public void setHardTimeout(Integer hardTimeout) {
        this.hardTimeout = hardTimeout;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
