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
@Document(collection = "user_sessions")
public class UserSessionDocument {
    @Id
    private UUID id;

    @Field("user_id")
    @Indexed
    private UUID userId;

    @Field("device_id")
    private UUID deviceId;

    @Field("session_token")
    @Indexed(unique = true)
    private String sessionToken;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("active")
    @Builder.Default
    private boolean active = true;

    @Field("started_at")
    @Builder.Default
    private LocalDateTime startedAt = LocalDateTime.now();

    @Field("last_activity_at")
    @Builder.Default
    private LocalDateTime lastActivityAt = LocalDateTime.now();

    @Field("expires_at")
    private LocalDateTime expiresAt;

    @Field("terminated")
    @Builder.Default
    private boolean terminated = false;

    @Field("terminated_at")
    private LocalDateTime terminatedAt;

    @Field("termination_reason")
    private String terminationReason;

    @Field("metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
}
