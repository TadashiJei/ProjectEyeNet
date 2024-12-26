package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.repository.mongodb.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsEngineService {
    private final NetworkMetricsRepository networkMetricsRepository;
    private final SecurityMetricsRepository securityMetricsRepository;
    private final TrafficAnalyticsRepository trafficAnalyticsRepository;
    private final DepartmentAnalyticsRepository departmentAnalyticsRepository;
    private final WebsiteAccessLogRepository websiteAccessLogRepository;

    public DepartmentAnalyticsDocument generateDepartmentAnalytics(UUID departmentId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusDays(1);

        // Get latest metrics
        NetworkMetricsDocument networkMetrics = getLatestNetworkMetrics(departmentId, dayAgo, now);
        SecurityMetricsDocument securityMetrics = getLatestSecurityMetrics(departmentId, dayAgo, now);
        TrafficAnalyticsDocument trafficAnalytics = getLatestTrafficAnalytics(departmentId, dayAgo, now);

        // Build department analytics
        DepartmentAnalyticsDocument analytics = DepartmentAnalyticsDocument.builder()
                .departmentId(departmentId)
                .timestamp(now)
                .networkMetrics(networkMetrics)
                .securityMetrics(securityMetrics)
                .trafficAnalytics(trafficAnalytics)
                .build();

        return departmentAnalyticsRepository.save(analytics);
    }

    public List<DepartmentAnalyticsDocument> getDepartmentAnalytics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return departmentAnalyticsRepository.findByDepartmentIdAndTimestampBetween(departmentId, start, end);
    }

    public List<TrafficAnalyticsDocument> getTrafficAnalytics(UUID deviceId, LocalDateTime start, LocalDateTime end) {
        return trafficAnalyticsRepository.findByDeviceIdAndTimestampBetween(deviceId, start, end);
    }

    public List<TrafficAnalyticsDocument> getTrafficAnalyticsByCategory(UUID deviceId, String category, LocalDateTime start, LocalDateTime end) {
        // Implement category-based filtering logic
        List<TrafficAnalyticsDocument> analytics = getTrafficAnalytics(deviceId, start, end);
        return analytics.stream()
                .filter(a -> category.equals(a.getCategory()))
                .toList();
    }

    public List<WebsiteAccessLogDocument> getAccessLogs(UUID deviceId, LocalDateTime start, LocalDateTime end) {
        return websiteAccessLogRepository.findByDeviceIdAndTimestampBetween(deviceId, start, end);
    }

    public List<WebsiteAccessLogDocument> getAccessLogsByCategory(UUID deviceId, String category, LocalDateTime start, LocalDateTime end) {
        // Implement category-based filtering logic
        List<WebsiteAccessLogDocument> logs = getAccessLogs(deviceId, start, end);
        return logs.stream()
                .filter(l -> category.equals(l.getCategory()))
                .toList();
    }

    public DepartmentAnalyticsDocument getDepartmentSummary(UUID departmentId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusDays(1);

        // Get latest metrics
        NetworkMetricsDocument networkMetrics = getLatestNetworkMetrics(departmentId, dayAgo, now);
        SecurityMetricsDocument securityMetrics = getLatestSecurityMetrics(departmentId, dayAgo, now);
        TrafficAnalyticsDocument trafficAnalytics = getLatestTrafficAnalytics(departmentId, dayAgo, now);

        // Build department analytics
        return DepartmentAnalyticsDocument.builder()
                .departmentId(departmentId)
                .timestamp(now)
                .networkMetrics(networkMetrics)
                .securityMetrics(securityMetrics)
                .trafficAnalytics(trafficAnalytics)
                .build();
    }

    public TrafficAnalyticsDocument getTrafficSummary(UUID deviceId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusDays(1);
        List<TrafficAnalyticsDocument> analytics = getTrafficAnalytics(deviceId, dayAgo, now);
        return analytics.isEmpty() ? createEmptyTrafficAnalytics(deviceId) : analytics.get(0);
    }

    private NetworkMetricsDocument getLatestNetworkMetrics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepository
                .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(departmentId, start, end);

        if (metrics.isEmpty()) {
            return createEmptyNetworkMetrics(departmentId);
        }

        NetworkMetricsDocument latest = metrics.get(0);
        return NetworkMetricsDocument.builder()
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .trafficMetrics(NetworkMetricsDocument.TrafficMetrics.builder()
                        .bytesTransferred(latest.getTotalBytes())
                        .packetsTransferred(0L)
                        .averageBandwidth(latest.getBandwidth())
                        .peakBandwidth(0L)
                        .build())
                .performanceMetrics(NetworkMetricsDocument.PerformanceMetrics.builder()
                        .latency(latest.getLatency())
                        .packetLoss(latest.getPacketLoss())
                        .jitter(latest.getJitter())
                        .activeConnections(0)
                        .build())
                .build();
    }

    private NetworkMetricsDocument createEmptyNetworkMetrics(UUID departmentId) {
        return NetworkMetricsDocument.builder()
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .trafficMetrics(NetworkMetricsDocument.TrafficMetrics.builder()
                        .bytesTransferred(0L)
                        .packetsTransferred(0L)
                        .averageBandwidth(0.0)
                        .peakBandwidth(0L)
                        .build())
                .performanceMetrics(NetworkMetricsDocument.PerformanceMetrics.builder()
                        .latency(0.0)
                        .packetLoss(0.0)
                        .jitter(0.0)
                        .activeConnections(0)
                        .build())
                .build();
    }

    private SecurityMetricsDocument getLatestSecurityMetrics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<SecurityMetricsDocument> metrics = securityMetricsRepository
                .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(departmentId, start, end);

        if (metrics.isEmpty()) {
            return createEmptySecurityMetrics(departmentId);
        }

        SecurityMetricsDocument latest = metrics.get(0);
        return SecurityMetricsDocument.builder()
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .vulnerabilityCount(latest.getVulnerabilityCount())
                .incidentCount(latest.getIncidentCount())
                .threatLevel(latest.getThreatLevel())
                .build();
    }

    private SecurityMetricsDocument createEmptySecurityMetrics(UUID departmentId) {
        return SecurityMetricsDocument.builder()
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .vulnerabilityCount(0)
                .incidentCount(0)
                .threatLevel(0.0)
                .build();
    }

    private TrafficAnalyticsDocument getLatestTrafficAnalytics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<TrafficAnalyticsDocument> analytics = trafficAnalyticsRepository
                .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(departmentId, start, end);

        if (analytics.isEmpty()) {
            return createEmptyTrafficAnalytics(departmentId);
        }

        TrafficAnalyticsDocument latest = analytics.get(0);
        return TrafficAnalyticsDocument.builder()
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .totalRequests(latest.getTotalRequests())
                .uniqueUsers(latest.getUniqueUsers())
                .averageResponseTime(latest.getAverageResponseTime())
                .errorRate(latest.getErrorRate())
                .build();
    }

    private TrafficAnalyticsDocument createEmptyTrafficAnalytics(UUID departmentId) {
        return TrafficAnalyticsDocument.builder()
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .totalRequests(0L)
                .uniqueUsers(0L)
                .averageResponseTime(0.0)
                .errorRate(0.0)
                .build();
    }
}
