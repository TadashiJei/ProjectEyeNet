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

    @Field("activity_type")
    private String activityType;

    @Field("description")
    private String description;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("status")
    private ActivityStatus status;

    @Field("details")
    private Map<String, Object> details;

    @Field("resource_type")
    private String resourceType;

    @Field("resource_id")
    private String resourceId;

    @Field("location")
    private GeoLocation location;

    public enum ActivityStatus {
        SUCCESS, FAILURE, PENDING, CANCELLED
    }

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
