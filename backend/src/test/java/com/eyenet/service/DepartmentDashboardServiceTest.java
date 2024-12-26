package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.model.entity.*;
import com.eyenet.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepartmentDashboardServiceTest {

    @Mock
    private NetworkMetricsRepository networkMetricsRepo;
    
    @Mock
    private PerformanceMetricsRepository performanceMetricsRepo;
    
    @Mock
    private WebsiteAccessLogRepository websiteAccessRepo;
    
    @Mock
    private DepartmentAnalyticsRepository departmentAnalyticsRepo;
    
    @Mock
    private AlertRepository alertRepo;
    
    @Mock
    private UserNetworkUsageRepository userNetworkUsageRepo;

    @InjectMocks
    private DepartmentDashboardService dashboardService;

    private UUID departmentId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        departmentId = UUID.randomUUID();
        startTime = LocalDateTime.now().minusHours(1);
        endTime = LocalDateTime.now();
    }

    @Test
    void getNetworkUsage_ShouldAggregateMetricsCorrectly() {
        // Arrange
        List<NetworkMetricsDocument> metrics = Arrays.asList(
            createNetworkMetrics(100L, 60L, 40L),
            createNetworkMetrics(200L, 120L, 80L)
        );
        
        when(networkMetricsRepo.findByDepartmentIdAndTimestampBetween(
            departmentId, startTime, endTime))
            .thenReturn(metrics);

        // Act
        NetworkUsageStats result = dashboardService.getNetworkUsage(departmentId, startTime, endTime);

        // Assert
        assertThat(result.getTotalBytesTransferred()).isEqualTo(300L);
        assertThat(result.getUploadBytes()).isEqualTo(180L);
        assertThat(result.getDownloadBytes()).isEqualTo(120L);
        assertThat(result.getProtocolUsage()).isNotEmpty();
        assertThat(result.getApplicationUsage()).isNotEmpty();
    }

    @Test
    void getPerformanceMetrics_ShouldReturnMetricsForPeriod() {
        // Arrange
        List<PerformanceMetrics> expectedMetrics = Arrays.asList(
            createPerformanceMetrics(),
            createPerformanceMetrics()
        );
        
        when(performanceMetricsRepo.findByDepartmentIdAndTimestampBetween(
            departmentId, startTime, endTime))
            .thenReturn(expectedMetrics);

        // Act
        List<PerformanceMetrics> result = dashboardService.getPerformanceMetrics(
            departmentId, startTime, endTime);

        // Assert
        assertThat(result).hasSize(2);
        verify(performanceMetricsRepo).findByDepartmentIdAndTimestampBetween(
            departmentId, startTime, endTime);
    }

    @Test
    void getDepartmentAlerts_ShouldFilterByMinimumSeverity() {
        // Arrange
        Alert.Severity minSeverity = Alert.Severity.WARNING;
        List<Alert> expectedAlerts = Arrays.asList(
            createAlert(Alert.Severity.WARNING),
            createAlert(Alert.Severity.CRITICAL)
        );
        
        when(alertRepo.findByDepartmentIdAndSeverityGreaterThanEqual(
            departmentId, minSeverity))
            .thenReturn(expectedAlerts);

        // Act
        List<Alert> result = dashboardService.getDepartmentAlerts(departmentId, minSeverity);

        // Assert
        assertThat(result).hasSize(2);
        verify(alertRepo).findByDepartmentIdAndSeverityGreaterThanEqual(
            departmentId, minSeverity);
    }

    @Test
    void getTopUsers_ShouldCalculateUserStatistics() {
        // Arrange
        User user = createUser();
        List<UserNetworkUsage> usageData = Arrays.asList(
            createUserNetworkUsage(user, 100L, 50L),
            createUserNetworkUsage(user, 200L, 100L)
        );
        
        when(userNetworkUsageRepo.findByDepartmentIdAndTimestampBetween(
            departmentId, startTime, endTime))
            .thenReturn(usageData);

        // Act
        List<UserUsageStats> result = dashboardService.getTopUsers(departmentId, startTime, endTime);

        // Assert
        assertThat(result).hasSize(1);
        UserUsageStats stats = result.get(0);
        assertThat(stats.getTotalBytesTransferred()).isEqualTo(450L);
        assertThat(stats.getUploadBytes()).isEqualTo(300L);
        assertThat(stats.getDownloadBytes()).isEqualTo(150L);
    }

    private NetworkMetricsDocument createNetworkMetrics(
            long totalBytes, long uploadBytes, long downloadBytes) {
        NetworkMetricsDocument metrics = new NetworkMetricsDocument();
        metrics.setTotalBytes(totalBytes);
        metrics.setUploadBytes(uploadBytes);
        metrics.setDownloadBytes(downloadBytes);
        metrics.setProtocolUsage(Map.of("TCP", 100L, "UDP", 50L));
        metrics.setApplicationUsage(Map.of("HTTP", 80L, "HTTPS", 70L));
        metrics.setTimestamp(LocalDateTime.now());
        return metrics;
    }

    private PerformanceMetrics createPerformanceMetrics() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.setCpuUsage(45.5);
        metrics.setMemoryUsage(65.8);
        metrics.setNetworkLatency(12.3);
        metrics.setTimestamp(LocalDateTime.now());
        return metrics;
    }

    private Alert createAlert(Alert.Severity severity) {
        Alert alert = new Alert();
        alert.setSeverity(severity);
        alert.setMessage("Test alert");
        alert.setStatus(Alert.AlertStatus.ACTIVE);
        return alert;
    }

    private User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        return user;
    }

    private UserNetworkUsage createUserNetworkUsage(User user, long upload, long download) {
        UserNetworkUsage usage = new UserNetworkUsage();
        usage.setUser(user);
        usage.setBytesUploaded(upload);
        usage.setBytesDownloaded(download);
        usage.setTimestamp(LocalDateTime.now());
        return usage;
    }
}
