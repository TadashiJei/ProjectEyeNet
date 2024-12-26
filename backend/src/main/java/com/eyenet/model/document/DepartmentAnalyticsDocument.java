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
@Document(collection = "department_analytics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAnalyticsDocument {
    @Id
    private UUID id;

    @Field("department_id")
    @Indexed
    private UUID departmentId;

    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;

    @Field("network_metrics")
    private NetworkMetricsDocument networkMetrics;

    @Field("security_metrics")
    private SecurityMetricsDocument securityMetrics;

    @Field("traffic_analytics")
    private TrafficAnalyticsDocument trafficAnalytics;

    @Field("total_users")
    private Integer totalUsers;

    @Field("active_users")
    private Integer activeUsers;

    @Field("total_devices")
    private Integer totalDevices;

    @Field("active_devices")
    private Integer activeDevices;

    @Field("bandwidth_usage")
    private BandwidthUsage bandwidthUsage;

    @Field("application_usage")
    private Map<String, Long> applicationUsage;

    @Field("protocol_usage")
    private Map<String, Long> protocolUsage;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BandwidthUsage {
        private Long totalBytes;
        private Long uploadBytes;
        private Long downloadBytes;
        private Double averageBandwidth;
        private Double peakBandwidth;
        private Double utilizationPercentage;
    }
}
