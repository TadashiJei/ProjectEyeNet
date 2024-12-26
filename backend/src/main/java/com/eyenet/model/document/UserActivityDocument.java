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
@Document(collection = "user_activities")
public class UserActivityDocument {
    @Id
    private UUID id;

    @Field("user_id")
    @Indexed
    private UUID userId;

    @Field("department_id")
    private UUID departmentId;

    @Field("type")
    private String type;

    @Field("action")
    private String action;

    @Field("description")
    private String description;

    @Field("source")
    private String source;

    @Field("ip_address")
    private String ipAddress;

    @Field("device_id")
    private UUID deviceId;

    @Field("session_id")
    private UUID sessionId;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("status")
    private String status;

    @Field("details")
    private Map<String, Object> details;

    @Field("metadata")
    private Map<String, Object> metadata;
}
