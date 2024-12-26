package com.eyenet.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ip_ranges")
public class IPRange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "start_ip", nullable = false)
    private String startIp;

    @Column(name = "end_ip", nullable = false)
    private String endIp;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

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
