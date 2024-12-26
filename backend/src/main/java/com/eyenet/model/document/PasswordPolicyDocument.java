package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_policies")
public class PasswordPolicyDocument {
    @Id
    private UUID id;

    @Field("min_length")
    private int minLength;

    @Field("max_length")
    private int maxLength;

    @Field("require_uppercase")
    private boolean requireUppercase;

    @Field("require_lowercase")
    private boolean requireLowercase;

    @Field("require_numbers")
    private boolean requireNumbers;

    @Field("require_special_chars")
    private boolean requireSpecialChars;

    @Field("max_age_days")
    private int maxAgeDays;

    @Field("prevent_reuse")
    private boolean preventReuse;

    @Field("reuse_count")
    private int reuseCount;

    @Field("lockout_threshold")
    private int lockoutThreshold;

    @Field("lockout_duration_minutes")
    private int lockoutDurationMinutes;

    @Field("department_id")
    private UUID departmentId;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("created_by")
    private UUID createdBy;

    @Field("updated_by")
    private UUID updatedBy;

    @Field("is_active")
    private boolean isActive;

    public boolean isRequireUppercase() {
        return requireUppercase;
    }

    public boolean isRequireLowercase() {
        return requireLowercase;
    }
}
