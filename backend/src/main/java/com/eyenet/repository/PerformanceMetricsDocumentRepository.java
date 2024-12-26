package com.eyenet.repository;

import com.eyenet.model.document.PerformanceMetricsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PerformanceMetricsDocumentRepository extends MongoRepository<PerformanceMetricsDocument, UUID> {
    List<PerformanceMetricsDocument> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<PerformanceMetricsDocument> findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
