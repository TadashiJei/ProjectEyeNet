package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsEngineService {
    private final NetworkMetricsDocumentRepository networkMetricsRepository;
    private final SecurityMetricsDocumentRepository securityMetricsRepository;
    private final PerformanceMetricsDocumentRepository performanceMetricsRepository;
    private final TrafficAnalyticsDocumentRepository trafficAnalyticsRepository;
    private final DepartmentAnalyticsDocumentRepository departmentAnalyticsRepository;

    public Map<String, Object> getDepartmentAnalytics(UUID departmentId) {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("networkMetrics", getCurrentNetworkMetrics(departmentId));
        metrics.put("securityMetrics", getCurrentSecurityMetrics(departmentId));
        metrics.put("performanceMetrics", getCurrentPerformanceMetrics(departmentId));
        metrics.put("trafficAnalytics", getCurrentTrafficAnalytics(departmentId));
        
        // Save analytics to database
        DepartmentAnalyticsDocument analytics = DepartmentAnalyticsDocument.builder()
                .id(UUID.randomUUID())
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .networkMetrics(metrics.get("networkMetrics"))
                .securityMetrics(metrics.get("securityMetrics"))
                .performanceMetrics(metrics.get("performanceMetrics"))
                .trafficAnalytics(metrics.get("trafficAnalytics"))
                .build();
        
        departmentAnalyticsRepository.save(analytics);
        
        return metrics;
    }

    private Map<String, Object> getCurrentNetworkMetrics(UUID departmentId) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepository
                .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(
                    departmentId, 
                    LocalDateTime.now().minusHours(1), 
                    LocalDateTime.now()
                );
        
        if (metrics.isEmpty()) {
            return Collections.emptyMap();
        }
        
        NetworkMetricsDocument latest = metrics.get(0);
        Map<String, Object> result = new HashMap<>();
        result.put("bandwidth", latest.getBandwidth());
        result.put("latency", latest.getLatency());
        result.put("packetLoss", latest.getPacketLoss());
        result.put("jitter", latest.getJitter());
        return result;
    }

    private Map<String, Object> getCurrentSecurityMetrics(UUID departmentId) {
        List<SecurityMetricsDocument> metrics = securityMetricsRepository
                .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(
                    departmentId,
                    LocalDateTime.now().minusHours(1),
                    LocalDateTime.now()
                );
        
        if (metrics.isEmpty()) {
            return Collections.emptyMap();
        }
        
        SecurityMetricsDocument latest = metrics.get(0);
        Map<String, Object> result = new HashMap<>();
        result.put("threatLevel", latest.getThreatLevel());
        result.put("vulnerabilities", latest.getVulnerabilities());
        result.put("incidents", latest.getIncidents());
        return result;
    }

    private Map<String, Object> getCurrentPerformanceMetrics(UUID departmentId) {
        List<PerformanceMetricsDocument> metrics = performanceMetricsRepository
                .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(
                    departmentId,
                    LocalDateTime.now().minusHours(1),
                    LocalDateTime.now()
                );
        
        if (metrics.isEmpty()) {
            return Collections.emptyMap();
        }
        
        PerformanceMetricsDocument latest = metrics.get(0);
        Map<String, Object> result = new HashMap<>();
        result.put("cpuUsage", latest.getCpuUsage());
        result.put("memoryUsage", latest.getMemoryUsage());
        result.put("diskUsage", latest.getDiskUsage());
        result.put("networkLatency", latest.getNetworkLatency());
        result.put("packetLoss", latest.getPacketLoss());
        result.put("throughput", latest.getThroughput());
        result.put("errorRate", latest.getErrorRate());
        result.put("queueDepth", latest.getQueueDepth());
        result.put("status", latest.getStatus());
        return result;
    }

    private Map<String, Object> getCurrentTrafficAnalytics(UUID departmentId) {
        List<TrafficAnalyticsDocument> analytics = trafficAnalyticsRepository
                .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(
                    departmentId,
                    LocalDateTime.now().minusHours(1),
                    LocalDateTime.now()
                );
        
        if (analytics.isEmpty()) {
            return Collections.emptyMap();
        }
        
        TrafficAnalyticsDocument latest = analytics.get(0);
        Map<String, Object> result = new HashMap<>();
        result.put("totalRequests", latest.getTotalRequests());
        result.put("uniqueUsers", latest.getUniqueUsers());
        result.put("avgResponseTime", latest.getAvgResponseTime());
        result.put("errorRate", latest.getErrorRate());
        result.put("topEndpoints", latest.getTopEndpoints());
        return result;
    }
}
