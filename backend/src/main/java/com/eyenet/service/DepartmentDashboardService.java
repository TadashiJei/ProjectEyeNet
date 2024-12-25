package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.model.entity.*;
import com.eyenet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
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
            totalBytes += metric.getTotalBytes();
            uploadBytes += metric.getUploadBytes();
            downloadBytes += metric.getDownloadBytes();
            
            // Aggregate protocol usage
            metric.getProtocolUsage().forEach((protocol, bytes) ->
                protocolUsage.merge(protocol, bytes, Long::sum));
            
            // Aggregate application usage
            metric.getApplicationUsage().forEach((app, bytes) ->
                applicationUsage.merge(app, bytes, Long::sum));
            
            usageOverTime.put(metric.getTimestamp(), metric.getTotalBytes());
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
        return departmentAnalyticsRepo.findByDepartmentIdAndPeriod(departmentId, start, end);
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
            .findLatestByDepartmentId(departmentId);
        
        // Get active alerts
        List<Alert> activeAlerts = alertRepo
            .findByDepartmentIdAndStatusAndSeverityGreaterThanEqual(
                departmentId,
                Alert.AlertStatus.ACTIVE,
                Alert.Severity.WARNING
            );
        
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
        List<UserNetworkUsage> usageData = userNetworkUsageRepo
            .findByDepartmentIdAndTimestampBetween(departmentId, start, end);
        
        Map<UUID, UserUsageStats> userStats = new HashMap<>();
        
        for (UserNetworkUsage usage : usageData) {
            userStats.computeIfAbsent(usage.getUser().getId(), id -> {
                UserUsageStats stats = new UserUsageStats();
                stats.setUserId(id);
                stats.setUsername(usage.getUser().getUsername());
                return stats;
            });
            
            UserUsageStats stats = userStats.get(usage.getUser().getId());
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
            BandwidthTrend trend = new BandwidthTrend();
            trend.setTimestamp(metric.getTimestamp());
            trend.setTotalBandwidth(metric.getBandwidthUsage());
            trend.setInboundBandwidth(metric.getInboundBandwidth());
            trend.setOutboundBandwidth(metric.getOutboundBandwidth());
            trend.setApplicationBandwidth(metric.getApplicationBandwidth());
            trend.setProtocolBandwidth(metric.getProtocolBandwidth());
            trend.setUtilizationPercentage(metric.getBandwidthUtilization());
            trends.add(trend);
        }
        
        return trends;
    }
}
