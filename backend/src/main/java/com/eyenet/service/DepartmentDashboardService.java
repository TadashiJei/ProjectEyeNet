package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.model.entity.*;
import com.eyenet.model.dto.BandwidthUsageDTO;
import com.eyenet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentDashboardService {
    private final NetworkMetricsRepository networkMetricsRepo;
    private final PerformanceMetricsRepository performanceMetricsRepo;
    private final WebsiteAccessLogRepository websiteAccessRepo;
    private final DepartmentAnalyticsRepository departmentAnalyticsRepo;
    private final AlertRepository alertRepo;
    private final UserNetworkUsageRepository userNetworkUsageRepo;

    public NetworkUsageStats getNetworkUsage(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepo.findByDepartmentIdAndTimestampBetween(
            departmentId, start, end);
        
        NetworkUsageStats stats = new NetworkUsageStats();
        // Aggregate metrics and populate stats
        long totalBytes = 0;
        long uploadBytes = 0;
        long downloadBytes = 0;
        Map<String, Long> protocolUsage = new HashMap<>();
        Map<String, Long> applicationUsage = new HashMap<>();
        Map<LocalDateTime, Long> usageOverTime = new TreeMap<>();
        
        for (NetworkMetricsDocument metric : metrics) {
            NetworkMetricsDocument.TrafficMetrics trafficMetrics = metric.getTrafficMetrics();
            if (trafficMetrics != null) {
                totalBytes += trafficMetrics.getBytesTransferred();
                uploadBytes += trafficMetrics.getBytesTransferred() / 2; // Simplified
                downloadBytes += trafficMetrics.getBytesTransferred() / 2; // Simplified
            }
            
            // Aggregate protocol usage
            if (metric.getProtocolUsage() != null) {
                metric.getProtocolUsage().forEach((protocol, bytes) ->
                    protocolUsage.merge(protocol, bytes, Long::sum));
            }
            
            // Aggregate application usage
            if (metric.getApplicationUsage() != null) {
                metric.getApplicationUsage().forEach((app, bytes) ->
                    applicationUsage.merge(app, bytes, Long::sum));
            }
            
            if (trafficMetrics != null) {
                usageOverTime.put(metric.getTimestamp(), trafficMetrics.getBytesTransferred());
            }
        }
        
        stats.setTotalBytesTransferred(totalBytes);
        stats.setUploadBytes(uploadBytes);
        stats.setDownloadBytes(downloadBytes);
        stats.setProtocolUsage(protocolUsage);
        stats.setApplicationUsage(applicationUsage);
        stats.setUsageOverTime(usageOverTime);
        
        return stats;
    }

    public List<PerformanceMetrics> getPerformanceMetrics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return performanceMetricsRepo.findByDepartmentIdAndTimestampBetween(departmentId, start, end);
    }

    public List<WebsiteAccessLog> getWebsiteAccess(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return websiteAccessRepo.findByDepartmentIdAndTimestampBetween(departmentId, start, end);
    }

    public DepartmentAnalytics getDepartmentAnalytics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return departmentAnalyticsRepo.findByDepartmentIdAndTimestampBetween(departmentId, start, end)
            .stream()
            .findFirst()
            .orElse(null);
    }

    public Map<String, Object> getDashboardSummary(UUID departmentId) {
        Map<String, Object> summary = new HashMap<>();
        
        // Get current network usage
        NetworkUsageStats currentUsage = getNetworkUsage(
            departmentId,
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now()
        );
        
        // Get latest performance metrics
        List<PerformanceMetrics> latestMetrics = performanceMetricsRepo
            .findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(
                departmentId,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now()
            );
        
        // Get active alerts
        List<Alert> activeAlerts = alertRepo
            .findByDepartmentIdAndStatus(departmentId, Alert.AlertStatus.NEW);
        
        summary.put("currentUsage", currentUsage);
        summary.put("performance", latestMetrics);
        summary.put("alerts", activeAlerts);
        
        return summary;
    }

    public List<Alert> getDepartmentAlerts(UUID departmentId, Alert.Severity minSeverity) {
        return minSeverity != null ?
            alertRepo.findByDepartmentIdAndSeverityGreaterThanEqual(departmentId, minSeverity) :
            alertRepo.findByDepartmentId(departmentId);
    }

    public List<UserUsageStats> getTopUsers(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<UserNetworkUsageDocument> usageData = userNetworkUsageRepo
            .findByDepartmentIdAndTimestampBetween(departmentId, start, end);
        
        Map<UUID, UserUsageStats> userStats = new HashMap<>();
        
        for (UserNetworkUsageDocument usage : usageData) {
            userStats.computeIfAbsent(usage.getUserId(), id -> {
                UserUsageStats stats = new UserUsageStats();
                stats.setUserId(id);
                stats.setUsername(usage.getUsername());
                return stats;
            });
            
            UserUsageStats stats = userStats.get(usage.getUserId());
            stats.setTotalBytesTransferred(stats.getTotalBytesTransferred() + 
                usage.getBytesUploaded() + usage.getBytesDownloaded());
            stats.setUploadBytes(stats.getUploadBytes() + usage.getBytesUploaded());
            stats.setDownloadBytes(stats.getDownloadBytes() + usage.getBytesDownloaded());
            stats.setLastActivity(usage.getTimestamp());
        }
        
        return new ArrayList<>(userStats.values());
    }

    public List<BandwidthTrend> getBandwidthTrends(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepo
            .findByDepartmentIdAndTimestampBetween(departmentId, start, end);
        
        List<BandwidthTrend> trends = new ArrayList<>();
        
        for (NetworkMetricsDocument metric : metrics) {
            NetworkMetricsDocument.TrafficMetrics trafficMetrics = metric.getTrafficMetrics();
            if (trafficMetrics != null) {
                BandwidthTrend trend = new BandwidthTrend();
                trend.setTimestamp(metric.getTimestamp());
                trend.setTotalBandwidth(trafficMetrics.getAverageBandwidth());
                trend.setInboundBandwidth(trafficMetrics.getAverageBandwidth() / 2); // Simplified
                trend.setOutboundBandwidth(trafficMetrics.getAverageBandwidth() / 2); // Simplified
                trend.setApplicationBandwidth(trafficMetrics.getAverageBandwidth());
                trend.setProtocolBandwidth(trafficMetrics.getAverageBandwidth());
                trend.setUtilizationPercentage(trafficMetrics.getPeakBandwidth().intValue());
                trends.add(trend);
            }
        }
        
        return trends;
    }

    private BandwidthUsageDTO mapToBandwidthUsageDTO(NetworkMetricsDocument metric) {
        NetworkMetricsDocument.TrafficMetrics trafficMetrics = metric.getTrafficMetrics();
        if (trafficMetrics == null) {
            return BandwidthUsageDTO.builder().build();
        }
        
        Double avgBandwidth = trafficMetrics.getAverageBandwidth();
        Map<String, Double> appBandwidth = new HashMap<>();
        Map<String, Double> protocolBandwidth = new HashMap<>();
        
        if (metric.getApplicationUsage() != null) {
            metric.getApplicationUsage().forEach((app, bytes) -> 
                appBandwidth.put(app, bytes.doubleValue()));
        }
        
        if (metric.getProtocolUsage() != null) {
            metric.getProtocolUsage().forEach((protocol, bytes) -> 
                protocolBandwidth.put(protocol, bytes.doubleValue()));
        }
        
        return BandwidthUsageDTO.builder()
                .inboundBandwidth(avgBandwidth / 2) // Simplified
                .outboundBandwidth(avgBandwidth / 2) // Simplified
                .applicationBandwidth(appBandwidth)
                .protocolBandwidth(protocolBandwidth)
                .bandwidthUtilization(trafficMetrics.getPeakBandwidth())
                .build();
    }
}
