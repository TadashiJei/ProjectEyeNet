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
@Table(name = "password_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "changed_by")
    private UUID changedBy;

    @Column(name = "change_reason")
    @Enumerated(EnumType.STRING)
    private ChangeReason changeReason;

    public enum ChangeReason {
        USER_REQUESTED,
        ADMIN_RESET,
        POLICY_EXPIRED,
        SECURITY_BREACH,
        INITIAL_SETUP
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
