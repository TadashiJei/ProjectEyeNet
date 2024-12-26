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
@Document(collection = "alerts")
public class AlertDocument {
    @Id
    private UUID id;

    @Field("type")
    private AlertType type;

    @Field("severity")
    private Severity severity;

    @Field("status")
    private AlertStatus status;

    @Field("message")
    private String message;

    @Field("details")
    private String details;

    @Field("device_id")
    private UUID deviceId;

    @DBRef
    @Field("device")
    private NetworkDeviceDocument device;

    @Field("acknowledged")
    private boolean acknowledged;

    @Field("acknowledged_by")
    private UUID acknowledgedBy;

    @Field("acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Field("acknowledgment_comment")
    private String acknowledgmentComment;

    @Field("resolved")
    private boolean resolved;

    @Field("resolved_by")
    private UUID resolvedBy;

    @Field("resolved_at")
    private LocalDateTime resolvedAt;

    @Field("resolution_comment")
    private String resolutionComment;

    @Field("timestamp")
    private LocalDateTime timestamp;

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

    public enum AlertType {
        SECURITY_BREACH,
        NETWORK_OUTAGE,
        DEVICE_FAILURE,
        PERFORMANCE_DEGRADATION,
        CONFIGURATION_CHANGE,
        THRESHOLD_VIOLATION,
        AUTHENTICATION_FAILURE,
        SYSTEM_ERROR,
        MAINTENANCE_REQUIRED,
        POLICY_VIOLATION
    }

    public enum Severity {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INFO
    }

    public enum AlertStatus {
        NEW,
        ACKNOWLEDGED,
        IN_PROGRESS,
        RESOLVED,
        CLOSED,
        REOPENED
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
}
