package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "network_devices")
public class NetworkDeviceDocument {
    @Id
    private UUID id;

    @Field("device_id")
    private String deviceId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("ip_address")
    private String ipAddress;

    @Field("mac_address")
    private String macAddress;

    @Field("model")
    private String model;

    @Field("manufacturer")
    private String manufacturer;

    @Field("firmware_version")
    private String firmwareVersion;

    @Field("is_active")
    private boolean isActive;

    @Field("last_seen")
    private LocalDateTime lastSeen;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("department_id")
    private UUID departmentId;

    @Field("location")
    private String location;

    @Field("status")
    private String status;

    @Field("ports")
    private List<PortDocument> ports;

    @Field("capabilities")
    private List<String> capabilities;
}
