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

    @Field("created_at")
    private LocalDateTime createdAt;
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
    private String priority;

    @Field("category")
    private String category;

    @Field("source")
    private String source;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("read")
    private boolean read;

    @Field("read_at")
    private LocalDateTime readAt;

    @Field("action_url")
    private String actionUrl;

    @Field("action_text")
    private String actionText;

    @Field("metadata")
    private Map<String, Object> metadata;

    @Field("details")
    private Map<String, Object> details;
}
