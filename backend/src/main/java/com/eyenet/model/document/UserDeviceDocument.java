package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_devices")
public class UserDeviceDocument {
    @Id
    private UUID id;

    @Field("user_id")
    @Indexed
    private UUID userId;

    @Field("device_identifier")
    @Indexed(unique = true)
    private String deviceIdentifier;

    @Field("device_type")
    private String deviceType;

    @Field("device_name")
    private String deviceName;

    @Field("os_version")
    private String osVersion;

    @Field("manufacturer")
    private String manufacturer;

    @Field("model")
    private String model;

    @Field("mac_address")
    private String macAddress;

    @Field("ip_address")
    private String ipAddress;

    @Field("status")
    @Builder.Default
    private String status = "INACTIVE";

    @Field("last_seen_at")
    @Builder.Default
    private LocalDateTime lastSeenAt = LocalDateTime.now();

    @Field("registered_at")
    @Builder.Default
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Field("terminated")
    @Builder.Default
    private boolean terminated = false;

    @Field("terminated_at")
    private LocalDateTime terminatedAt;

    @Field("trusted")
    @Builder.Default
    private boolean trusted = false;

    @Field("metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
}
