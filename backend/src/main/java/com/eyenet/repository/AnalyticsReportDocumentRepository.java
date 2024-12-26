package com.eyenet.repository;

import com.eyenet.model.document.AnalyticsReportDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsReportDocumentRepository extends MongoRepository<AnalyticsReportDocument, UUID> {
    List<AnalyticsReportDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
