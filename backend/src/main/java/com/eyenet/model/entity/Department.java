package com.eyenet.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "departments")
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBandwidthQuota() {
        return bandwidthQuota;
    }

    public void setBandwidthQuota(Long bandwidthQuota) {
        this.bandwidthQuota = bandwidthQuota;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getMaxBandwidth() {
        return maxBandwidth;
    }

    public void setMaxBandwidth(Long maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    public Long getDailyDataLimit() {
        return dailyDataLimit;
    }

    public void setDailyDataLimit(Long dailyDataLimit) {
        this.dailyDataLimit = dailyDataLimit;
    }

    public Boolean getSocialMediaBlocked() {
        return socialMediaBlocked;
    }

    public void setSocialMediaBlocked(Boolean socialMediaBlocked) {
        this.socialMediaBlocked = socialMediaBlocked;
    }

    public Boolean getStreamingBlocked() {
        return streamingBlocked;
    }

    public void setStreamingBlocked(Boolean streamingBlocked) {
        this.streamingBlocked = streamingBlocked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
