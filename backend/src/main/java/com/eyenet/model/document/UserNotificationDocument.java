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
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_notifications")
public class UserNotificationDocument {
    @Id
    private UUID id;

    @Field("user_id")
    @Indexed
    private UUID userId;

    @Field("type")
    private String type;

    @Field("title")
    private String title;

    @Field("message")
    private String message;

    @Field("priority")
    private Priority priority;

    @Field("source")
    private String source;

    @Field("source_id")
    private String sourceId;

    @Field("metadata")
    private Map<String, Object> metadata;

    @Field("read")
    private boolean read;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("read_at")
    private LocalDateTime readAt;

    @Field("expires_at")
    private LocalDateTime expiresAt;

    @Field("action_url")
    private String actionUrl;

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }
}
