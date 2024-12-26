package com.eyenet.repository;

import com.eyenet.model.document.DepartmentAnalyticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentAnalyticsDocumentRepository extends MongoRepository<DepartmentAnalyticsDocument, UUID> {
    List<DepartmentAnalyticsDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
    List<DepartmentAnalyticsDocument> findByDepartmentIdAndMetricTypeAndTimestampBetween(UUID departmentId, String metricType, LocalDateTime start, LocalDateTime end);
}
