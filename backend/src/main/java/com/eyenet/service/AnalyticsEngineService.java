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
                .bandwidth(latest.getBandwidth())
                .latency(latest.getLatency())
                .packetLoss(latest.getPacketLoss())
                .jitter(latest.getJitter())
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

    private NetworkMetricsDocument createEmptyNetworkMetrics(UUID departmentId) {
        return NetworkMetricsDocument.builder()
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .bandwidth(0.0)
                .latency(0.0)
                .packetLoss(0.0)
                .jitter(0.0)
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
