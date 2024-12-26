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
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "network_configs")
public class NetworkConfigDocument {
    @Id
    private UUID id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("parameters")
    private Map<String, String> parameters;

    @DBRef
    @Field("applied_to")
    private NetworkDeviceDocument appliedTo;

    @Field("last_applied")
    private LocalDateTime lastApplied;

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

    @Field("version")
    private int version;
}
