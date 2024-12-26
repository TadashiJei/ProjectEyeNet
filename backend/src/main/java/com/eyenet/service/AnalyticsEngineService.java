package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.repository.mongodb.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
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

    public List<TrafficAnalyticsDocument> getTrafficAnalytics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return trafficAnalyticsRepository.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(departmentId, start, end);
    }

    public List<WebsiteAccessLogDocument> getAccessLogs(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return websiteAccessLogRepository.findByDepartmentIdAndTimestampBetween(departmentId, start, end);
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

    public TrafficAnalyticsDocument getTrafficSummary(UUID departmentId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusDays(1);
        List<TrafficAnalyticsDocument> analytics = getTrafficAnalytics(departmentId, dayAgo, now);
        return analytics.isEmpty() ? createEmptyTrafficAnalytics(departmentId) : analytics.get(0);
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
                        .bytesTransferred(latest.getTrafficMetrics().getBytesTransferred())
                        .packetsTransferred(latest.getTrafficMetrics().getPacketsTransferred())
                        .averageBandwidth(latest.getTrafficMetrics().getAverageBandwidth())
                        .peakBandwidth(latest.getTrafficMetrics().getPeakBandwidth())
                        .build())
                .performanceMetrics(NetworkMetricsDocument.PerformanceMetrics.builder()
                        .latency(latest.getPerformanceMetrics().getLatency())
                        .packetLoss(latest.getPerformanceMetrics().getPacketLoss())
                        .jitter(latest.getPerformanceMetrics().getJitter())
                        .activeConnections(latest.getPerformanceMetrics().getActiveConnections())
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
                        .activeConnections(0L)
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
                .vulnerabilityCount(0L)
                .incidentCount(0L)
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
