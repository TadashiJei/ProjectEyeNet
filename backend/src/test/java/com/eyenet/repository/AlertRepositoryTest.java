package com.eyenet.repository;

import com.eyenet.model.document.AlertDocument;
import com.eyenet.repository.mongodb.AlertRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class AlertRepositoryTest {

    @Autowired
    private AlertRepository alertRepository;

    @Test
    void testSaveAndFindById() {
        // Create test alert
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .type(AlertDocument.AlertType.NETWORK_OUTAGE)
                .severity(AlertDocument.Severity.HIGH)
                .status(AlertDocument.AlertStatus.NEW)
                .message("Test alert")
                .details("Test details")
                .deviceId(UUID.randomUUID())
                .acknowledged(false)
                .timestamp(LocalDateTime.now())
                .build();

        // Save alert
        AlertDocument savedAlert = alertRepository.save(alert);
        assertThat(savedAlert).isNotNull();
        assertThat(savedAlert.getId()).isEqualTo(alert.getId());

        // Find by ID
        Optional<AlertDocument> foundAlert = alertRepository.findById(alert.getId());
        assertThat(foundAlert).isPresent();
        assertThat(foundAlert.get().getMessage()).isEqualTo("Test alert");
    }

    @Test
    void testFindByDeviceId() {
        UUID deviceId = UUID.randomUUID();
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .deviceId(deviceId)
                .type(AlertDocument.AlertType.DEVICE_FAILURE)
                .severity(AlertDocument.Severity.HIGH)
                .status(AlertDocument.AlertStatus.NEW)
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByDeviceId(deviceId, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).getDeviceId()).isEqualTo(deviceId);
    }

    @Test
    void testFindByType() {
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .type(AlertDocument.AlertType.SECURITY_BREACH)
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByType(AlertDocument.AlertType.SECURITY_BREACH, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).getType()).isEqualTo(AlertDocument.AlertType.SECURITY_BREACH);
    }

    @Test
    void testFindBySeverity() {
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .severity(AlertDocument.Severity.CRITICAL)
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findBySeverity(AlertDocument.Severity.CRITICAL, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).getSeverity()).isEqualTo(AlertDocument.Severity.CRITICAL);
    }

    @Test
    void testFindByStatus() {
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .status(AlertDocument.AlertStatus.ACKNOWLEDGED)
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByStatus(AlertDocument.AlertStatus.ACKNOWLEDGED, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).getStatus()).isEqualTo(AlertDocument.AlertStatus.ACKNOWLEDGED);
    }

    @Test
    void testFindByAcknowledged() {
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .acknowledged(true)
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByAcknowledged(true, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).isAcknowledged()).isTrue();
    }

    @Test
    void testFindByTimestampBetween() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByTimestampBetween(start, end, pageable);
        assertThat(alerts).isNotEmpty();
    }

    @Test
    void testFindByDeviceIdAndTimestampBetween() {
        UUID deviceId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .deviceId(deviceId)
                .timestamp(LocalDateTime.now())
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByDeviceIdAndTimestampBetween(deviceId, start, end, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).getDeviceId()).isEqualTo(deviceId);
    }

    @Test
    void testFindByDeviceIdAndType() {
        UUID deviceId = UUID.randomUUID();
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .deviceId(deviceId)
                .type(AlertDocument.AlertType.PERFORMANCE_DEGRADATION)
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByDeviceIdAndType(deviceId, AlertDocument.AlertType.PERFORMANCE_DEGRADATION, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).getDeviceId()).isEqualTo(deviceId);
        assertThat(alerts.getContent().get(0).getType()).isEqualTo(AlertDocument.AlertType.PERFORMANCE_DEGRADATION);
    }

    @Test
    void testFindByDeviceIdAndSeverity() {
        UUID deviceId = UUID.randomUUID();
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .deviceId(deviceId)
                .severity(AlertDocument.Severity.HIGH)
                .build();

        alertRepository.save(alert);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertDocument> alerts = alertRepository.findByDeviceIdAndSeverity(deviceId, AlertDocument.Severity.HIGH, pageable);
        assertThat(alerts).isNotEmpty();
        assertThat(alerts.getContent().get(0).getDeviceId()).isEqualTo(deviceId);
        assertThat(alerts.getContent().get(0).getSeverity()).isEqualTo(AlertDocument.Severity.HIGH);
    }

    @Test
    void testDeleteAlert() {
        AlertDocument alert = AlertDocument.builder()
                .id(UUID.randomUUID())
                .type(AlertDocument.AlertType.SYSTEM_ERROR)
                .severity(AlertDocument.Severity.LOW)
                .build();

        AlertDocument savedAlert = alertRepository.save(alert);
        assertThat(savedAlert).isNotNull();

        alertRepository.deleteById(alert.getId());

        Optional<AlertDocument> deletedAlert = alertRepository.findById(alert.getId());
        assertThat(deletedAlert).isEmpty();
    }
}
