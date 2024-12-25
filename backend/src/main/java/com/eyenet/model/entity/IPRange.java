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
@Table(name = "ip_ranges")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPRange {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "start_ip", nullable = false)
    private String startIP;

    @Column(name = "end_ip", nullable = false)
    private String endIP;

    @Column(name = "subnet_mask")
    private String subnetMask;

    @Column(name = "gateway")
    private String gateway;

    @Column(name = "dns_servers")
    private String dnsServers;

    @Column(name = "vlan_id")
    private Integer vlanId;

    @Column(name = "is_reserved")
    private Boolean isReserved;

    @Column(name = "description")
    private String description;

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
