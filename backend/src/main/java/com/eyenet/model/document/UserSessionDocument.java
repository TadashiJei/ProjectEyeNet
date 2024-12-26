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
@Document(collection = "user_sessions")
public class UserSessionDocument {
    @Id
    private UUID id;

    @Field("user_id")
    @Indexed
    private UUID userId;

    @Field("token")
    private String token;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Field("expires_at")
    private LocalDateTime expiresAt;

    @Field("active")
    private boolean active;

    @Field("device_info")
    private DeviceInfo deviceInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeviceInfo {
        private String deviceType;
        private String operatingSystem;
        private String browser;
        private String deviceId;
        private String location;
    }
}
