package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.model.dto.BandwidthTrend;
import com.eyenet.model.dto.BandwidthUsageDTO;
import com.eyenet.model.dto.NetworkUsageStats;
import com.eyenet.model.dto.UserUsageStats;
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
    private final NetworkMetricsDocumentRepository networkMetricsRepo;
    private final PerformanceMetricsDocumentRepository performanceMetricsRepo;
    private final WebsiteAccessLogDocumentRepository websiteAccessRepo;
    private final DepartmentAnalyticsDocumentRepository departmentAnalyticsRepo;
    private final AlertDocumentRepository alertRepo;
    private final UserNetworkUsageDocumentRepository userNetworkUsageRepo;

    public NetworkUsageStats getNetworkUsage(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepo.findByDepartmentIdAndTimestampBetween(
                departmentId, start, end);
        
        return NetworkUsageStats.builder()
                .totalBytesIn(metrics.stream().mapToLong(NetworkMetricsDocument::getBytesIn).sum())
                .totalBytesOut(metrics.stream().mapToLong(NetworkMetricsDocument::getBytesOut).sum())
                .averageBandwidth(metrics.stream().mapToDouble(NetworkMetricsDocument::getBandwidth).average().orElse(0.0))
                .peakBandwidth(metrics.stream().mapToDouble(NetworkMetricsDocument::getBandwidth).max().orElse(0.0))
                .totalConnections(metrics.stream().mapToLong(NetworkMetricsDocument::getConnectionCount).sum())
                .build();
    }

    public List<PerformanceMetricsDocument> getPerformanceMetrics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return performanceMetricsRepo.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(departmentId, start, end);
    }

    public List<WebsiteAccessLogDocument> getWebsiteAccess(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return websiteAccessRepo.findByDepartmentIdAndTimestampBetween(departmentId, start, end);
    }

    public List<DepartmentAnalyticsDocument> getDepartmentAnalytics(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return departmentAnalyticsRepo.findByDepartmentIdAndTimestampBetween(departmentId, start, end);
    }

    public List<AlertDocument> getAlerts(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        return alertRepo.findByDepartmentIdAndTimestampBetween(departmentId, start, end);
    }

    public UserUsageStats getUserUsageStats(UUID departmentId, UUID userId, LocalDateTime start, LocalDateTime end) {
        List<UserNetworkUsageDocument> usageData = userNetworkUsageRepo.findByUserIdAndDepartmentIdAndTimestampBetween(
                userId, departmentId, start, end);
        
        return UserUsageStats.builder()
                .totalBytesTransferred(usageData.stream().mapToLong(UserNetworkUsageDocument::getTotalBytes).sum())
                .averageBandwidth(usageData.stream().mapToDouble(UserNetworkUsageDocument::getAverageBandwidth).average().orElse(0.0))
                .peakBandwidth(usageData.stream().mapToDouble(UserNetworkUsageDocument::getPeakBandwidth).max().orElse(0.0))
                .totalSessions(usageData.stream().mapToLong(UserNetworkUsageDocument::getSessionCount).sum())
                .build();
    }

    public BandwidthTrend getBandwidthTrend(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepo.findByDepartmentIdAndTimestampBetween(
                departmentId, start, end);
        
        return BandwidthTrend.builder()
                .timestamps(metrics.stream().map(NetworkMetricsDocument::getTimestamp).toList())
                .bandwidthValues(metrics.stream().map(NetworkMetricsDocument::getBandwidth).toList())
                .build();
    }

    public BandwidthUsageDTO getBandwidthUsage(UUID departmentId, LocalDateTime start, LocalDateTime end) {
        List<NetworkMetricsDocument> metrics = networkMetricsRepo.findByDepartmentIdAndTimestampBetween(
                departmentId, start, end);
        
        double totalBandwidth = metrics.stream()
                .mapToDouble(NetworkMetricsDocument::getBandwidth)
                .sum();
        
        double averageBandwidth = metrics.stream()
                .mapToDouble(NetworkMetricsDocument::getBandwidth)
                .average()
                .orElse(0.0);
        
        return BandwidthUsageDTO.builder()
                .totalBandwidth(totalBandwidth)
                .averageBandwidth(averageBandwidth)
                .build();
    }
}
