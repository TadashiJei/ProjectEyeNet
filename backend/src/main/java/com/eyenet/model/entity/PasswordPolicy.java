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
@Table(name = "password_policies")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordPolicy {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "min_length", nullable = false)
    private int minLength;

    @Column(name = "require_uppercase")
    private boolean requireUppercase;

    @Column(name = "require_lowercase")
    private boolean requireLowercase;

    @Column(name = "require_numbers")
    private boolean requireNumbers;

    @Column(name = "require_special_chars")
    private boolean requireSpecialChars;

    @Column(name = "special_chars_allowed")
    private String specialCharsAllowed;

    @Column(name = "max_age_days")
    private Integer maxAgeDays;

    @Column(name = "prevent_reuse_count")
    private Integer preventReuseCount;

    @Column(name = "lockout_threshold")
    private Integer lockoutThreshold;

    @Column(name = "lockout_duration_minutes")
    private Integer lockoutDurationMinutes;

    @Column(name = "is_default")
    private boolean isDefault;

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
