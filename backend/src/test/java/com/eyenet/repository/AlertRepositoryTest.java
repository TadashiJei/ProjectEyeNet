package com.eyenet.repository;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.repository.mongodb.AlertRepository;
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
class AlertRepositoryTest {

    @Autowired
    private AlertRepository alertRepository;

    private AlertDocument testAlert;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        alertRepository.deleteAll();

        testAlert = AlertDocument.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(UUID.randomUUID().toString())
                .type("PERFORMANCE")
                .severity("HIGH")
                .message("High CPU Usage")
                .details("CPU usage exceeded 90%")
                .status("ACTIVE")
                .acknowledged(false)
                .acknowledgedBy(null)
                .acknowledgedAt(null)
                .timestamp(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        alertRepository.save(testAlert);
    }

    @Test
    void findById_shouldReturnAlert() {
        Optional<AlertDocument> found = alertRepository.findById(testAlert.getId());
        assertTrue(found.isPresent());
        assertEquals(testAlert.getDeviceId(), found.get().getDeviceId());
        assertEquals(testAlert.getType(), found.get().getType());
        assertEquals(testAlert.getSeverity(), found.get().getSeverity());
        assertEquals(testAlert.getMessage(), found.get().getMessage());
        assertEquals(testAlert.getDetails(), found.get().getDetails());
        assertEquals(testAlert.getStatus(), found.get().getStatus());
        assertEquals(testAlert.isAcknowledged(), found.get().isAcknowledged());
        assertEquals(testAlert.getAcknowledgedBy(), found.get().getAcknowledgedBy());
        assertEquals(testAlert.getAcknowledgedAt(), found.get().getAcknowledgedAt());
        assertEquals(testAlert.getTimestamp(), found.get().getTimestamp());
        assertEquals(testAlert.getCreatedAt(), found.get().getCreatedAt());
        assertEquals(testAlert.getUpdatedAt(), found.get().getUpdatedAt());
    }

    @Test
    void findByDeviceId_shouldReturnAlerts() {
        List<AlertDocument> found = alertRepository.findByDeviceId(testAlert.getDeviceId());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findByType_shouldReturnAlerts() {
        List<AlertDocument> found = alertRepository.findByType(testAlert.getType());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findBySeverity_shouldReturnAlerts() {
        List<AlertDocument> found = alertRepository.findBySeverity(testAlert.getSeverity());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findByStatus_shouldReturnAlerts() {
        List<AlertDocument> found = alertRepository.findByStatus(testAlert.getStatus());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findByAcknowledged_shouldReturnAlerts() {
        List<AlertDocument> found = alertRepository.findByAcknowledged(testAlert.isAcknowledged());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findByTimestampBetween_shouldReturnAlerts() {
        LocalDateTime start = testTime.minusHours(1);
        LocalDateTime end = testTime.plusHours(1);
        List<AlertDocument> found = alertRepository.findByTimestampBetween(start, end);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findByDeviceIdAndTimestampBetween_shouldReturnAlerts() {
        LocalDateTime start = testTime.minusHours(1);
        LocalDateTime end = testTime.plusHours(1);
        List<AlertDocument> found = alertRepository.findByDeviceIdAndTimestampBetween(
                testAlert.getDeviceId(), start, end);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findByDeviceIdAndType_shouldReturnAlerts() {
        List<AlertDocument> found = alertRepository.findByDeviceIdAndType(
                testAlert.getDeviceId(), testAlert.getType());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findByDeviceIdAndSeverity_shouldReturnAlerts() {
        List<AlertDocument> found = alertRepository.findByDeviceIdAndSeverity(
                testAlert.getDeviceId(), testAlert.getSeverity());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testAlert.getId(), found.get(0).getId());
    }

    @Test
    void findAll_withPagination_shouldReturnPagedAlerts() {
        Page<AlertDocument> page = alertRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "timestamp")));
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(testAlert.getId(), page.getContent().get(0).getId());
    }

    @Test
    void save_shouldCreateNewAlert() {
        AlertDocument newAlert = AlertDocument.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(UUID.randomUUID().toString())
                .type("SECURITY")
                .severity("MEDIUM")
                .message("Unauthorized Access Attempt")
                .details("Multiple failed login attempts detected")
                .status("PENDING")
                .acknowledged(true)
                .acknowledgedBy("admin")
                .acknowledgedAt(testTime)
                .timestamp(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        AlertDocument saved = alertRepository.save(newAlert);
        assertNotNull(saved.getId());
        assertEquals(newAlert.getDeviceId(), saved.getDeviceId());
        assertEquals(newAlert.getType(), saved.getType());
        assertEquals(newAlert.getSeverity(), saved.getSeverity());
        assertEquals(newAlert.getMessage(), saved.getMessage());
        assertEquals(newAlert.getDetails(), saved.getDetails());
        assertEquals(newAlert.getStatus(), saved.getStatus());
        assertEquals(newAlert.isAcknowledged(), saved.isAcknowledged());
        assertEquals(newAlert.getAcknowledgedBy(), saved.getAcknowledgedBy());
        assertEquals(newAlert.getAcknowledgedAt(), saved.getAcknowledgedAt());
        assertEquals(newAlert.getTimestamp(), saved.getTimestamp());
        assertEquals(newAlert.getCreatedAt(), saved.getCreatedAt());
        assertEquals(newAlert.getUpdatedAt(), saved.getUpdatedAt());
    }

    @Test
    void delete_shouldRemoveAlert() {
        alertRepository.delete(testAlert);
        Optional<AlertDocument> found = alertRepository.findById(testAlert.getId());
        assertFalse(found.isPresent());
    }
}
