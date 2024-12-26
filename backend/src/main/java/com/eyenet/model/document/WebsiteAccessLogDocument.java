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
@Document(collection = "website_access_logs")
public class WebsiteAccessLogDocument {
    @Id
    private UUID id;

    @DBRef
    @Field("device")
    private NetworkDeviceDocument device;

    @Field("device_id")
    private UUID deviceId;

    @Field("user_id")
    private UUID userId;

    @Field("url")
    private String url;

    @Field("domain")
    private String domain;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("http_method")
    private String httpMethod;

    @Field("status_code")
    private Integer statusCode;

    @Field("response_time")
    private Long responseTime;

    @Field("bytes_transferred")
    private Long bytesTransferred;

    @Field("category")
    private WebsiteCategory category;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("referrer")
    private String referrer;

    @Field("department_id")
    private UUID departmentId;

    @Field("metadata")
    private Map<String, String> metadata;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    public enum WebsiteCategory {
        BUSINESS,
        SOCIAL_MEDIA,
        NEWS,
        ENTERTAINMENT,
        EDUCATION,
        SHOPPING,
        TECHNOLOGY,
        ADULT,
        GAMING,
        OTHER
    }

    public WebsiteCategory getCategory() {
        return category;
    }
}
