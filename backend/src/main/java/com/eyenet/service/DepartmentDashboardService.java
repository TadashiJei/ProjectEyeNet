package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.model.dto.*;
import com.eyenet.repository.mongodb.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentDashboardService {
    private final NetworkMetricsRepository networkMetricsRepository;
    private final SecurityMetricsRepository securityMetricsRepository;
    private final TrafficAnalyticsRepository trafficAnalyticsRepository;
    private final AlertRepository alertRepository;
    private final UserNetworkUsageRepository userNetworkUsageRepo;

    public DashboardMetricsDTO getDepartmentMetrics(UUID departmentId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusDays(1);

        List<NetworkMetricsDocument> networkMetrics = networkMetricsRepository
                .findByDepartmentIdAndTimestampBetween(departmentId, dayAgo, now);

        double avgBandwidth = networkMetrics.stream()
                .mapToDouble(NetworkMetricsDocument::getBandwidth)
                .average()
                .orElse(0.0);

        double avgLatency = networkMetrics.stream()
                .mapToDouble(NetworkMetricsDocument::getLatency)
                .average()
                .orElse(0.0);

        return DashboardMetricsDTO.builder()
                .averageBandwidth(avgBandwidth)
                .averageLatency(avgLatency)
                .activeAlerts(alertRepository.countByDepartmentIdAndStatus(departmentId, AlertDocument.AlertStatus.ACTIVE))
                .build();
    }

    public NetworkMetricsDTO getNetworkMetrics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepository
                .findByDepartmentIdAndTimestampBetween(departmentId, start, end);

        return NetworkMetricsDTO.builder()
                .bandwidth(calculateAverageBandwidth(metrics))
                .latency(calculateAverageLatency(metrics))
                .packetLoss(calculateAveragePacketLoss(metrics))
                .jitter(calculateAverageJitter(metrics))
                .build();
    }

    public SecurityMetricsDTO getSecurityMetrics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<SecurityMetricsDocument> metrics = securityMetricsRepository
                .findByDepartmentIdAndTimestampBetween(departmentId, start, end);

        return SecurityMetricsDTO.builder()
                .vulnerabilities(countVulnerabilities(metrics))
                .incidents(countIncidents(metrics))
                .threatLevel(calculateThreatLevel(metrics))
                .build();
    }

    public TrafficAnalyticsDTO getTrafficAnalytics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<TrafficAnalyticsDocument> analytics = trafficAnalyticsRepository
                .findByDepartmentIdAndTimestampBetween(departmentId, start, end);

        return TrafficAnalyticsDTO.builder()
                .totalRequests(calculateTotalRequests(analytics))
                .uniqueUsers(calculateUniqueUsers(analytics))
                .averageResponseTime(calculateAverageResponseTime(analytics))
                .errorRate(calculateErrorRate(analytics))
                .build();
    }

    public List<AlertDTO> getDepartmentAlerts(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<AlertDocument> alerts = alertRepository
                .findByDepartmentIdAndTimestampBetween(departmentId, start, end);

        return alerts.stream()
                .map(this::mapToAlertDTO)
                .collect(Collectors.toList());
    }

    private double calculateAverageBandwidth(List<NetworkMetricsDocument> metrics) {
        return metrics.stream()
                .mapToDouble(NetworkMetricsDocument::getBandwidth)
                .average()
                .orElse(0.0);
    }

    private double calculateAverageLatency(List<NetworkMetricsDocument> metrics) {
        return metrics.stream()
                .mapToDouble(NetworkMetricsDocument::getLatency)
                .average()
                .orElse(0.0);
    }

    private double calculateAveragePacketLoss(List<NetworkMetricsDocument> metrics) {
        return metrics.stream()
                .mapToDouble(NetworkMetricsDocument::getPacketLoss)
                .average()
                .orElse(0.0);
    }

    private double calculateAverageJitter(List<NetworkMetricsDocument> metrics) {
        return metrics.stream()
                .mapToDouble(NetworkMetricsDocument::getJitter)
                .average()
                .orElse(0.0);
    }

    private int countVulnerabilities(List<SecurityMetricsDocument> metrics) {
        return metrics.stream()
                .mapToInt(SecurityMetricsDocument::getVulnerabilityCount)
                .sum();
    }

    private int countIncidents(List<SecurityMetricsDocument> metrics) {
        return metrics.stream()
                .mapToInt(SecurityMetricsDocument::getIncidentCount)
                .sum();
    }

    private double calculateThreatLevel(List<SecurityMetricsDocument> metrics) {
        return metrics.stream()
                .mapToDouble(SecurityMetricsDocument::getThreatLevel)
                .average()
                .orElse(0.0);
    }

    private long calculateTotalRequests(List<TrafficAnalyticsDocument> analytics) {
        return analytics.stream()
                .mapToLong(TrafficAnalyticsDocument::getTotalRequests)
                .sum();
    }

    private long calculateUniqueUsers(List<TrafficAnalyticsDocument> analytics) {
        return analytics.stream()
                .mapToLong(TrafficAnalyticsDocument::getUniqueUsers)
                .max()
                .orElse(0L);
    }

    private double calculateAverageResponseTime(List<TrafficAnalyticsDocument> analytics) {
        return analytics.stream()
                .mapToDouble(TrafficAnalyticsDocument::getAverageResponseTime)
                .average()
                .orElse(0.0);
    }

    private double calculateErrorRate(List<TrafficAnalyticsDocument> analytics) {
        return analytics.stream()
                .mapToDouble(TrafficAnalyticsDocument::getErrorRate)
                .average()
                .orElse(0.0);
    }

    private AlertDTO mapToAlertDTO(AlertDocument alert) {
        return AlertDTO.builder()
                .id(alert.getId())
                .title(alert.getTitle())
                .message(alert.getMessage())
                .severity(alert.getSeverity())
                .status(alert.getStatus())
                .timestamp(alert.getCreatedAt())
                .build();
    }
}
