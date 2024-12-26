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
@Table(name = "flow_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private NetworkDevice device;

    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "cookie")
    private Long cookie;

    @Column(name = "match_criteria", nullable = false)
    private String matchCriteria;

    @Column(name = "actions", nullable = false)
    private String actions;

    @Column(name = "idle_timeout")
    private Integer idleTimeout;

    @Column(name = "hard_timeout")
    private Integer hardTimeout;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FlowRuleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "bytes_count")
    private Long bytesCount;

    @Column(name = "packets_count")
    private Long packetsCount;

    @Column(name = "last_matched")
    private LocalDateTime lastMatched;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    public enum FlowRuleStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        ERROR,
        DELETED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = FlowRuleStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
