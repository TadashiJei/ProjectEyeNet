package com.eyenet.repository;

import com.eyenet.model.document.TrafficAnalyticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrafficAnalyticsDocumentRepository extends MongoRepository<TrafficAnalyticsDocument, UUID> {
    List<TrafficAnalyticsDocument> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<TrafficAnalyticsDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
