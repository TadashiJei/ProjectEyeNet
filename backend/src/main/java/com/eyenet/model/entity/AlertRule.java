package com.eyenet.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "alert_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "alert_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Alert.AlertType alertType;

    @Column(name = "severity", nullable = false)
    @Enumerated(EnumType.STRING)
    private Alert.Severity severity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "metric_name", nullable = false)
    private String metricName;

    @Column(name = "condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(name = "threshold_value", nullable = false)
    private Double thresholdValue;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "cooldown_minutes")
    private Integer cooldownMinutes;

    @Column(name = "enabled")
    @Builder.Default
    private boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Condition {
        GREATER_THAN,
        LESS_THAN,
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN_OR_EQUALS
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
