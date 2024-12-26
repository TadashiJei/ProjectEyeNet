package com.eyenet.repository;

import com.eyenet.model.document.SecurityMetricsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SecurityMetricsDocumentRepository extends MongoRepository<SecurityMetricsDocument, UUID> {
    List<SecurityMetricsDocument> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<SecurityMetricsDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
