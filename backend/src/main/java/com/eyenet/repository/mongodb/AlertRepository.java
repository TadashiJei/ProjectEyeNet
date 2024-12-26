package com.eyenet.repository.mongodb;

import com.eyenet.model.document.AlertDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends MongoRepository<AlertDocument, UUID> {
    List<AlertDocument> findByDeviceId(UUID deviceId);
    
    Page<AlertDocument> findByDeviceId(UUID deviceId, Pageable pageable);
    
    List<AlertDocument> findByDeviceIdAndAcknowledged(UUID deviceId, boolean acknowledged);
    
    List<AlertDocument> findByDeviceIdAndType(UUID deviceId, String type);
    
    List<AlertDocument> findByDeviceIdAndSeverity(UUID deviceId, String severity);
    
    @Query("{'deviceId': ?0, 'createdAt': {$gte: ?1, $lt: ?2}}")
    List<AlertDocument> findByDeviceIdAndTimeRange(
            UUID deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'deviceId': ?0, 'severity': {$in: ['HIGH', 'CRITICAL']}}")
    List<AlertDocument> findHighPriorityAlerts(UUID deviceId);
    
    @Query("{'deviceId': ?0, 'acknowledged': false, 'severity': ?1}")
    List<AlertDocument> findUnacknowledgedAlertsBySeverity(UUID deviceId, String severity);
    
    List<AlertDocument> findByDepartmentId(UUID departmentId);
    
    Page<AlertDocument> findByDepartmentId(UUID departmentId, Pageable pageable);

    List<AlertDocument> findByDepartmentIdAndStatus(UUID departmentId, AlertDocument.AlertStatus status);

    List<AlertDocument> findByStatusAndSeverity(AlertDocument.AlertStatus status, AlertDocument.Severity severity);

    @Query("{'departmentId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<AlertDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);

    Long countByDepartmentIdAndStatus(UUID departmentId, AlertDocument.AlertStatus status);
}
