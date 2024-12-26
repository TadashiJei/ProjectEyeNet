package com.eyenet.repository;

import com.eyenet.model.document.PerformanceMetricsDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class PerformanceMetricsRepositoryTest {

    @Autowired
    private PerformanceMetricsRepository performanceMetricsRepository;

    private PerformanceMetricsDocument testMetrics;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        performanceMetricsRepository.deleteAll();

        testMetrics = PerformanceMetricsDocument.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(UUID.randomUUID().toString())
                .cpuUsage(50.0)
                .memoryUsage(75.0)
                .diskUsage(60.0)
                .networkThroughput(1000.0)
                .packetLoss(0.1)
                .latency(5.0)
                .timestamp(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        performanceMetricsRepository.save(testMetrics);
    }

    @Test
    void findById_shouldReturnMetrics() {
        Optional<PerformanceMetricsDocument> found = performanceMetricsRepository.findById(testMetrics.getId());
        assertTrue(found.isPresent());
        assertEquals(testMetrics.getDeviceId(), found.get().getDeviceId());
        assertEquals(testMetrics.getCpuUsage(), found.get().getCpuUsage());
        assertEquals(testMetrics.getMemoryUsage(), found.get().getMemoryUsage());
        assertEquals(testMetrics.getDiskUsage(), found.get().getDiskUsage());
        assertEquals(testMetrics.getNetworkThroughput(), found.get().getNetworkThroughput());
        assertEquals(testMetrics.getPacketLoss(), found.get().getPacketLoss());
        assertEquals(testMetrics.getLatency(), found.get().getLatency());
        assertEquals(testMetrics.getTimestamp(), found.get().getTimestamp());
        assertEquals(testMetrics.getCreatedAt(), found.get().getCreatedAt());
        assertEquals(testMetrics.getUpdatedAt(), found.get().getUpdatedAt());
    }

    @Test
    void findByDeviceId_shouldReturnMetrics() {
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByDeviceId(testMetrics.getDeviceId());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findByTimestampBetween_shouldReturnMetrics() {
        LocalDateTime start = testTime.minusHours(1);
        LocalDateTime end = testTime.plusHours(1);
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByTimestampBetween(start, end);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findByDeviceIdAndTimestampBetween_shouldReturnMetrics() {
        LocalDateTime start = testTime.minusHours(1);
        LocalDateTime end = testTime.plusHours(1);
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByDeviceIdAndTimestampBetween(
                testMetrics.getDeviceId(), start, end);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findByCpuUsageGreaterThan_shouldReturnMetrics() {
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByCpuUsageGreaterThan(40.0);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findByMemoryUsageGreaterThan_shouldReturnMetrics() {
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByMemoryUsageGreaterThan(70.0);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findByDiskUsageGreaterThan_shouldReturnMetrics() {
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByDiskUsageGreaterThan(50.0);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findByPacketLossGreaterThan_shouldReturnMetrics() {
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByPacketLossGreaterThan(0.05);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findByLatencyGreaterThan_shouldReturnMetrics() {
        List<PerformanceMetricsDocument> found = performanceMetricsRepository.findByLatencyGreaterThan(4.0);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testMetrics.getId(), found.get(0).getId());
    }

    @Test
    void findAll_withPagination_shouldReturnPagedMetrics() {
        Page<PerformanceMetricsDocument> page = performanceMetricsRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "timestamp")));
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(testMetrics.getId(), page.getContent().get(0).getId());
    }

    @Test
    void save_shouldCreateNewMetrics() {
        PerformanceMetricsDocument newMetrics = PerformanceMetricsDocument.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(UUID.randomUUID().toString())
                .cpuUsage(80.0)
                .memoryUsage(90.0)
                .diskUsage(70.0)
                .networkThroughput(2000.0)
                .packetLoss(0.2)
                .latency(10.0)
                .timestamp(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        PerformanceMetricsDocument saved = performanceMetricsRepository.save(newMetrics);
        assertNotNull(saved.getId());
        assertEquals(newMetrics.getDeviceId(), saved.getDeviceId());
        assertEquals(newMetrics.getCpuUsage(), saved.getCpuUsage());
        assertEquals(newMetrics.getMemoryUsage(), saved.getMemoryUsage());
        assertEquals(newMetrics.getDiskUsage(), saved.getDiskUsage());
        assertEquals(newMetrics.getNetworkThroughput(), saved.getNetworkThroughput());
        assertEquals(newMetrics.getPacketLoss(), saved.getPacketLoss());
        assertEquals(newMetrics.getLatency(), saved.getLatency());
        assertEquals(newMetrics.getTimestamp(), saved.getTimestamp());
        assertEquals(newMetrics.getCreatedAt(), saved.getCreatedAt());
        assertEquals(newMetrics.getUpdatedAt(), saved.getUpdatedAt());
    }

    @Test
    void delete_shouldRemoveMetrics() {
        performanceMetricsRepository.delete(testMetrics);
        Optional<PerformanceMetricsDocument> found = performanceMetricsRepository.findById(testMetrics.getId());
        assertFalse(found.isPresent());
    }
}
