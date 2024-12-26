package com.eyenet.repository;

import com.eyenet.model.document.AlertDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends MongoRepository<AlertDocument, UUID> {
    List<AlertDocument> findByDepartmentId(UUID departmentId);
    List<AlertDocument> findByDepartmentIdAndStatus(UUID departmentId, AlertDocument.AlertStatus status);
    List<AlertDocument> findByDepartmentIdAndSeverity(UUID departmentId, AlertDocument.Severity severity);
    List<AlertDocument> findByDepartmentIdAndSeverityGreaterThanEqual(UUID departmentId, AlertDocument.Severity minSeverity);
}
