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

    @Field("device_name")
    private String deviceName;

    @Field("device_type")
    private String deviceType;

    @Field("device_id")
    private String deviceId;

    @Field("push_token")
    private String pushToken;

    @Field("operating_system")
    private String operatingSystem;

    @Field("os_version")
    private String osVersion;

    @Field("last_used_at")
    private LocalDateTime lastUsedAt;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("enabled")
    private boolean enabled;

    @Field("trusted")
    private boolean trusted;

    @Field("last_ip_address")
    private String lastIpAddress;

    @Field("location")
    private GeoLocation location;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GeoLocation {
        private String country;
        private String city;
        private String latitude;
        private String longitude;
    }
}
