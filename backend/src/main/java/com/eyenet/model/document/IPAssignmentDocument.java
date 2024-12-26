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
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ip_assignments")
public class IPAssignmentDocument {
    @Id
    private UUID id;

    @Field("ip_address")
    private String ipAddress;

    @Field("mac_address")
    private String macAddress;

    @Field("hostname")
    private String hostname;

    @DBRef
    @Field("ip_range")
    private IPRangeDocument ipRange;

    @Field("status")
    private IPStatus status;

    @Field("lease_start")
    private LocalDateTime leaseStart;

    @Field("lease_end")
    private LocalDateTime leaseEnd;

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

    @Field("description")
    private String description;

    @Field("is_static")
    private boolean isStatic;

    public enum IPStatus {
        AVAILABLE,
        ASSIGNED,
        RESERVED,
        EXPIRED
    }
}
