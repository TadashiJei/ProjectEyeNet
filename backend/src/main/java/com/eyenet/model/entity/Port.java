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
@Table(name = "ports")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Port {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private NetworkDevice device;

    @Column(name = "port_number", nullable = false)
    private Integer portNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "speed_mbps")
    private Integer speedMbps;

    @Column(name = "is_up")
    private Boolean isUp;

    @Column(name = "vlan_id")
    private Integer vlanId;

    @Column(name = "port_type")
    @Enumerated(EnumType.STRING)
    private PortType type;

    @Column(name = "qos_policy")
    private String qosPolicy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum PortType {
        ACCESS,
        TRUNK,
        HYBRID
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
