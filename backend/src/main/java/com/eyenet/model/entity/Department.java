package com.eyenet.model.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "bandwidth_quota")
    private Long bandwidthQuota;

    @Column(nullable = false)
    private Integer priority;

    @Column(name = "max_bandwidth")
    private Long maxBandwidth;

    @Column(name = "daily_data_limit")
    private Long dailyDataLimit;

    @Column(name = "social_media_blocked")
    private Boolean socialMediaBlocked;

    @Column(name = "streaming_blocked")
    private Boolean streamingBlocked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
