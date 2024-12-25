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
@Table(name = "qos_policies")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QoSPolicy {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "min_bandwidth_mbps")
    private Integer minBandwidthMbps;

    @Column(name = "max_bandwidth_mbps")
    private Integer maxBandwidthMbps;

    @Column(name = "burst_size_mb")
    private Integer burstSizeMb;

    @Column(name = "dscp_marking")
    private Integer dscpMarking;

    @Column(name = "traffic_class")
    @Enumerated(EnumType.STRING)
    private TrafficClass trafficClass;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TrafficClass {
        VOICE,
        VIDEO,
        CRITICAL_DATA,
        BULK_DATA,
        BEST_EFFORT,
        SCAVENGER
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
