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
@Table(name = "ip_assignments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPAssignment {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ip_range_id", nullable = false)
    private IPRange ipRange;

    @Column(name = "ip_address", nullable = false, unique = true)
    private String ipAddress;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "lease_start")
    private LocalDateTime leaseStart;

    @Column(name = "lease_end")
    private LocalDateTime leaseEnd;

    @Column(name = "is_static")
    private Boolean isStatic;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private IPStatus status;

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

    public enum IPStatus {
        AVAILABLE,
        ASSIGNED,
        RESERVED,
        EXPIRED
    }
}
