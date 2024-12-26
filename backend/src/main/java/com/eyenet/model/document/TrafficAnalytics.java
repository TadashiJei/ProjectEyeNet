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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "traffic_analytics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrafficAnalytics {
    @Id
    private String id;

    @Field("department_id")
    @Indexed
    private UUID departmentId;

    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;

    @Field("interval_minutes")
    private int intervalMinutes;

    @Field("total_traffic_bytes")
    private long totalTrafficBytes;

    @Field("bandwidth_usage_mbps")
    private double bandwidthUsageMbps;

    @Field("peak_bandwidth_mbps")
    private double peakBandwidthMbps;

    @Field("traffic_patterns")
    private List<TrafficPattern> patterns;

    @Field("top_talkers")
    private List<TopTalker> topTalkers;

    @Field("application_stats")
    private Map<String, ApplicationStats> applicationStatistics;

    private String pattern;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TrafficPattern {
        private String pattern;
        private long occurrences;
        private double averageBytes;
        private String sourceIp;
        private String destinationIp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopTalker {
        private String ipAddress;
        private long bytesTransferred;
        private int connectionCount;
        private Map<String, Long> services;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApplicationStats {
        private long bytesTransferred;
        private int activeUsers;
        private double averageResponseTime;
        private int errorCount;
    }
}
