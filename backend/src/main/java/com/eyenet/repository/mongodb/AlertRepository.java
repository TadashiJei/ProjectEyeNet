package com.eyenet.repository.mongodb;

import com.eyenet.model.document.AlertDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends MongoRepository<AlertDocument, UUID> {
    Page<AlertDocument> findByType(AlertDocument.AlertType type, Pageable pageable);
    Page<AlertDocument> findBySeverity(AlertDocument.Severity severity, Pageable pageable);
    Page<AlertDocument> findByStatus(AlertDocument.AlertStatus status, Pageable pageable);
    Page<AlertDocument> findByAcknowledged(boolean acknowledged, Pageable pageable);
    Page<AlertDocument> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<AlertDocument> findByDeviceId(UUID deviceId, Pageable pageable);
    Page<AlertDocument> findByDeviceIdAndTimestampBetween(UUID deviceId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<AlertDocument> findByDeviceIdAndType(UUID deviceId, AlertDocument.AlertType type, Pageable pageable);
    Page<AlertDocument> findByDeviceIdAndSeverity(UUID deviceId, AlertDocument.Severity severity, Pageable pageable);
    Page<AlertDocument> findByDeviceIdAndStatus(UUID deviceId, AlertDocument.AlertStatus status, Pageable pageable);
    
    List<AlertDocument> findByTypeAndSeverityAndStatusAndAcknowledged(
        AlertDocument.AlertType type,
        AlertDocument.Severity severity,
        AlertDocument.AlertStatus status,
        boolean acknowledged
    );
    
    long countByTypeAndSeverityAndStatusAndAcknowledged(
        AlertDocument.AlertType type,
        AlertDocument.Severity severity,
        AlertDocument.AlertStatus status,
        boolean acknowledged
    );
}
