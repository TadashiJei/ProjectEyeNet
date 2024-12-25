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
@Document(collection = "department_analytics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAnalytics {
    @Id
    private String id;

    @Field("department_id")
    @Indexed
    private UUID departmentId;

    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;

    @Field("interval_type")
    private String intervalType; // hourly, daily, weekly, monthly

    @Field("bandwidth_usage")
    private BandwidthUsage bandwidthUsage;

    @Field("application_usage")
    private List<ApplicationUsage> applicationUsage;

    @Field("security_metrics")
    private SecurityMetrics securityMetrics;

    @Field("compliance_metrics")
    private ComplianceMetrics complianceMetrics;

    @Field("user_activity")
    private UserActivityMetrics userActivity;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BandwidthUsage {
        private long totalBytesTransferred;
        private double averageBandwidthMbps;
        private double peakBandwidthMbps;
        private double quotaUtilizationPercent;
        private Map<String, Long> usageByCategory;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApplicationUsage {
        private String applicationName;
        private long bytesTransferred;
        private int uniqueUsers;
        private double averageResponseTime;
        private int totalRequests;
        private int blockedRequests;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SecurityMetrics {
        private int malwareDetections;
        private int suspiciousActivities;
        private int policyViolations;
        private Map<String, Integer> securityIncidentsByType;
        private List<String> compromisedIPs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ComplianceMetrics {
        private boolean quotaCompliant;
        private boolean securityPolicyCompliant;
        private List<String> violations;
        private Map<String, Integer> violationsByPolicy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserActivityMetrics {
        private int activeUsers;
        private int totalSessions;
        private double averageSessionDuration;
        private Map<String, Integer> activityByTimeOfDay;
        private List<String> topActiveUsers;
    }
}
