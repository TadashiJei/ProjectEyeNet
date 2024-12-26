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
@Document(collection = "ip_ranges")
public class IPRangeDocument {
    @Id
    private UUID id;

    @Field("start_ip")
    private String startIP;

    @Field("end_ip")
    private String endIP;

    @Field("subnet_mask")
    private String subnetMask;

    @Field("gateway")
    private String gateway;

    @Field("department_id")
    private UUID departmentId;

    @Field("description")
    private String description;

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

    @Field("vlan_id")
    private Integer vlanId;
}
