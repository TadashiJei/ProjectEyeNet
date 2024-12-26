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
import java.util.UUID;

@Data
@Document(collection = "network_metrics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkMetricsDocument {
    @Id
    private UUID id;

    @Indexed
    private UUID departmentId;

    @Indexed
    private LocalDateTime timestamp;

    private TrafficMetrics trafficMetrics;

    private List<WebsiteAccess> websiteAccess;

    private Map<String, Long> protocolUsage;

    private Map<String, Long> applicationUsage;

    private PerformanceMetrics performanceMetrics;

    public Long getTotalBytes() {
        return trafficMetrics != null ? trafficMetrics.getBytesTransferred() : 0L;
    }

    public Long getUploadBytes() {
        return trafficMetrics != null ? trafficMetrics.getBytesTransferred() / 2 : 0L; // Simplified
    }

    public Long getDownloadBytes() {
        return trafficMetrics != null ? trafficMetrics.getBytesTransferred() / 2 : 0L; // Simplified
    }

    public Map<String, Long> getApplicationUsage() {
        return applicationUsage;
    }

    public double getBandwidth() {
        return trafficMetrics != null ? trafficMetrics.getAverageBandwidth() : 0.0;
    }

    public double getLatency() {
        return performanceMetrics != null ? performanceMetrics.getLatency() : 0.0;
    }

    public double getPacketLoss() {
        return performanceMetrics != null ? performanceMetrics.getPacketLoss() : 0.0;
    }

    public double getJitter() {
        return performanceMetrics != null ? performanceMetrics.getJitter() : 0.0;
    }

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
