package com.eyenet.model.entity;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "system_configs")
@Data
public class SystemConfig {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;

    @Column
    private String description;

    @Column(name = "config_type")
    @Enumerated(EnumType.STRING)
    private ConfigType type;

    @Column(nullable = false)
    private boolean encrypted;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum ConfigType {
        SYSTEM,
        SECURITY,
        NETWORK,
        MONITORING,
        NOTIFICATION
    }
}
