package com.eyenet.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_network_usage")
@Data
public class UserNetworkUsage {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bytes_uploaded")
    private long bytesUploaded;

    @Column(name = "bytes_downloaded")
    private long bytesDownloaded;

    @Column(name = "active_connections")
    private int activeConnections;

    @Column(name = "bandwidth_usage")
    private double bandwidthUsage;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
