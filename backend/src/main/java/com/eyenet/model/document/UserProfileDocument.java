package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_profiles")
public class UserProfileDocument {
    @Id
    private UUID id;

    @Field("username")
    private String username;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @DBRef
    @Field("department")
    private DepartmentDocument department;

    @Field("department_id")
    private UUID departmentId;

    @Field("role")
    private UserRole role;

    @Field("status")
    private UserStatus status;

    @Field("last_login")
    private LocalDateTime lastLogin;

    @Field("failed_login_attempts")
    private Integer failedLoginAttempts;

    @Field("account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Field("password_last_changed")
    private LocalDateTime passwordLastChanged;

    @Field("must_change_password")
    private boolean mustChangePassword;

    @Field("permissions")
    private Set<String> permissions;

    @Field("preferences")
    private Map<String, String> preferences;

    @Field("metadata")
    private Map<String, String> metadata;

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

    public enum UserRole {
        ADMIN,
        NETWORK_ENGINEER,
        SECURITY_ANALYST,
        OPERATOR,
        VIEWER,
        GUEST
    }

    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        LOCKED,
        PENDING_ACTIVATION,
        SUSPENDED
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public DepartmentDocument getDepartment() {
        return department;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDepartment(DepartmentDocument department) {
        this.department = department;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
