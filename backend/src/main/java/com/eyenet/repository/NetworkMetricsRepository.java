package com.eyenet.repository;

import com.eyenet.model.document.NetworkMetricsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NetworkMetricsRepository extends MongoRepository<NetworkMetricsDocument, String> {
    List<NetworkMetricsDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
