package com.eyenet.repository.mongodb;

import com.eyenet.model.document.TrafficAnalyticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrafficAnalyticsRepository extends MongoRepository<TrafficAnalyticsDocument, UUID> {
    List<TrafficAnalyticsDocument> findByDepartmentId(UUID departmentId);
    List<TrafficAnalyticsDocument> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<TrafficAnalyticsDocument> findByDepartmentIdAndTimestampBetweenOrderByTimestampDesc(UUID departmentId, LocalDateTime start, LocalDateTime end);
    Optional<TrafficAnalyticsDocument> findFirstByDepartmentIdOrderByTimestampDesc(UUID departmentId);
}
