package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Data
@Document(collection = "network_metrics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkMetricsDocument {
    @Id
    private String id;

    @Indexed
    private String departmentId;

    @Indexed
    private LocalDateTime timestamp;

    private TrafficMetrics trafficMetrics;

    private List<WebsiteAccess> websiteAccess;

    private Map<String, Long> protocolUsage;

    private PerformanceMetrics performanceMetrics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TrafficMetrics {
        private Long bytesTransferred;
        private Long packetsTransferred;
        private Double averageBandwidth;
        private Long peakBandwidth;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WebsiteAccess {
        private String domain;
        private Long accessCount;
        private Long dataTransferred;
        private LocalDateTime lastAccessed;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PerformanceMetrics {
        private Double latency;
        private Double packetLoss;
        private Double jitter;
        private Integer activeConnections;
    }
}
