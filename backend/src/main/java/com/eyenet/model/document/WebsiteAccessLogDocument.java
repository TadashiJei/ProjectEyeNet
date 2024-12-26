package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private UUID userId;
    private UUID departmentId;
    private String url;
    private String method;
    private String userAgent;
    private String ipAddress;
    private String referrer;
    private Integer statusCode;
    private Long responseTime;
    private Long bytesTransferred;
    private Map<String, String> headers;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
