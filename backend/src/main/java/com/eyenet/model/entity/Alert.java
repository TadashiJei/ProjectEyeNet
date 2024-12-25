package com.eyenet.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "alerts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "alert_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(name = "severity", nullable = false)
    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "details")
    private String details;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "threshold_value")
    private Double thresholdValue;

    @Column(name = "current_value")
    private Double currentValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by")
    private UUID resolvedBy;

    @Column(name = "resolution_notes")
    private String resolutionNotes;

    public enum AlertType {
        BANDWIDTH_EXCEEDED,
        HIGH_LATENCY,
        PACKET_LOSS,
        DEVICE_DOWN,
        PORT_DOWN,
        SECURITY_BREACH,
        CONFIGURATION_CHANGE,
        QUOTA_EXCEEDED,
        PERFORMANCE_DEGRADATION,
        UNAUTHORIZED_ACCESS
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
        FALSE_POSITIVE
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = AlertStatus.NEW;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
