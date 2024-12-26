package com.eyenet.service;

import com.eyenet.model.document.*;
import com.eyenet.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentDashboardServiceTest {

    @Mock
    private NetworkMetricsDocumentRepository networkMetricsRepo;
    
    @Mock
    private SecurityMetricsDocumentRepository securityMetricsRepo;
    
    @Mock
    private PerformanceMetricsDocumentRepository performanceMetricsRepo;
    
    @Mock
    private TrafficAnalyticsDocumentRepository trafficAnalyticsRepo;
    
    @Mock
    private AlertDocumentRepository alertRepo;

    @InjectMocks
    private DepartmentDashboardService dashboardService;

    private UUID departmentId;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        departmentId = UUID.randomUUID();
        start = LocalDateTime.now().minusHours(1);
        end = LocalDateTime.now();
    }

    @Test
    void getDepartmentMetrics_ShouldReturnAllMetrics() {
        // Given
        when(networkMetricsRepo.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(any(), any(), any()))
            .thenReturn(Collections.singletonList(createNetworkMetrics()));
        
        when(securityMetricsRepo.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(any(), any(), any()))
            .thenReturn(Collections.singletonList(createSecurityMetrics()));
        
        when(performanceMetricsRepo.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(any(), any(), any()))
            .thenReturn(Collections.singletonList(createPerformanceMetrics()));
        
        when(trafficAnalyticsRepo.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(any(), any(), any()))
            .thenReturn(Collections.singletonList(createTrafficAnalytics()));

        // When
        Map<String, Object> result = dashboardService.getDepartmentMetrics(departmentId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsKeys("networkMetrics", "securityMetrics", "performanceMetrics", "trafficAnalytics");
    }

    @Test
    void getPerformanceMetrics_ShouldReturnMetricsForPeriod() {
        // Given
        List<PerformanceMetricsDocument> expectedMetrics = Arrays.asList(
            createPerformanceMetrics(),
            createPerformanceMetrics()
        );
        
        when(performanceMetricsRepo.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(
            departmentId, start, end))
            .thenReturn(expectedMetrics);

        // When
        List<PerformanceMetricsDocument> result = dashboardService.getPerformanceMetrics(departmentId, start, end);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    void getNetworkMetrics_ShouldReturnMetricsForPeriod() {
        // Given
        List<NetworkMetricsDocument> expectedMetrics = Arrays.asList(
            createNetworkMetrics(),
            createNetworkMetrics()
        );
        
        when(networkMetricsRepo.findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(
            departmentId, start, end))
            .thenReturn(expectedMetrics);

        // When
        List<NetworkMetricsDocument> result = dashboardService.getNetworkMetrics(departmentId, start, end);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    private NetworkMetricsDocument createNetworkMetrics() {
        return NetworkMetricsDocument.builder()
                .id(UUID.randomUUID())
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .bandwidth(100.0)
                .latency(5.0)
                .packetLoss(0.1)
                .jitter(2.0)
                .build();
    }

    private SecurityMetricsDocument createSecurityMetrics() {
        return SecurityMetricsDocument.builder()
                .id(UUID.randomUUID())
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .threatLevel("LOW")
                .vulnerabilities(Collections.singletonList("Test vulnerability"))
                .incidents(Collections.singletonList("Test incident"))
                .build();
    }

    private PerformanceMetricsDocument createPerformanceMetrics() {
        return PerformanceMetricsDocument.builder()
                .id(UUID.randomUUID())
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .cpuUsage(50.0)
                .memoryUsage(60.0)
                .diskUsage(70.0)
                .networkLatency(5.0)
                .packetLoss(0.1)
                .throughput(1000.0)
                .errorRate(0.01)
                .queueDepth(10.0)
                .status("HEALTHY")
                .build();
    }

    private TrafficAnalyticsDocument createTrafficAnalytics() {
        return TrafficAnalyticsDocument.builder()
                .id(UUID.randomUUID())
                .departmentId(departmentId)
                .timestamp(LocalDateTime.now())
                .totalRequests(1000L)
                .uniqueUsers(100L)
                .avgResponseTime(50.0)
                .errorRate(0.01)
                .topEndpoints(Collections.singletonMap("/api/test", 100L))
                .build();
    }
}
