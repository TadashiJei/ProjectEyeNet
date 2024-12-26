package com.eyenet.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "network_devices")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "device_id", unique = true, nullable = false)
    private String deviceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "device_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "mac_address", nullable = false)
    private String macAddress;

    @Column(name = "location")
    private String location;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Port> ports = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum DeviceType {
        SWITCH,
        ROUTER,
        FIREWALL,
        ACCESS_POINT,
        GATEWAY
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
