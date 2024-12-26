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
@Table(name = "flow_rule_templates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowRuleTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "template_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TemplateType type;

    @Column(name = "match_criteria", nullable = false)
    private String matchCriteria;

    @Column(name = "actions", nullable = false)
    private String actions;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "idle_timeout")
    private Integer idleTimeout;

    @Column(name = "hard_timeout")
    private Integer hardTimeout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    public enum TemplateType {
        QOS,
        SECURITY,
        LOAD_BALANCING,
        FORWARDING,
        MONITORING,
        CUSTOM
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
