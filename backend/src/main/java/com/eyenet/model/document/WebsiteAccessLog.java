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
@Document(collection = "website_access_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteAccessLog {
    @Id
    private UUID id;

    @Field("department_id")
    @Indexed
    private UUID departmentId;

    @Field("user_id")
    @Indexed
    private UUID userId;

    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;

    @Field("source_ip")
    private String sourceIp;

    @Field("destination_url")
    private String destinationUrl;

    @Field("http_method")
    private String httpMethod;

    @Field("status_code")
    private Integer statusCode;

    @Field("response_time_ms")
    private Long responseTimeMs;

    @Field("bytes_transferred")
    private Long bytesTransferred;

    @Field("user_agent")
    private String userAgent;

    @Field("category")
    private String category;

    @Field("is_blocked")
    private Boolean isBlocked;

    @Field("geo_location")
    private GeoLocation geoLocation;

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
