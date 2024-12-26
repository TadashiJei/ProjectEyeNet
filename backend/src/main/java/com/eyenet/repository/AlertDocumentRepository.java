package com.eyenet.repository;

import com.eyenet.model.document.AlertDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertDocumentRepository extends MongoRepository<AlertDocument, UUID> {
    List<AlertDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
    List<AlertDocument> findByDeviceIdAndTimestampBetween(UUID deviceId, LocalDateTime start, LocalDateTime end);
    List<AlertDocument> findByStatusAndTimestampBetween(String status, LocalDateTime start, LocalDateTime end);
    List<AlertDocument> findByDepartmentIdAndStatusAndTimestampBetween(UUID departmentId, String status, LocalDateTime start, LocalDateTime end);
}
