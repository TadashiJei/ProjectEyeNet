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
@Document(collection = "departments")
public class DepartmentDocument {
    @Id
    private UUID id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("parent_department_id")
    private UUID parentDepartmentId;

    @DBRef
    @Field("parent_department")
    private DepartmentDocument parentDepartment;

    @Field("head_user_id")
    private UUID headUserId;

    @DBRef
    @Field("head_user")
    private UserProfileDocument headUser;

    @Field("location")
    private String location;

    @Field("contact_email")
    private String contactEmail;

    @Field("contact_phone")
    private String contactPhone;

    @Field("budget_code")
    private String budgetCode;

    @Field("cost_center")
    private String costCenter;

    @Field("permissions")
    private Set<String> permissions;

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
}
