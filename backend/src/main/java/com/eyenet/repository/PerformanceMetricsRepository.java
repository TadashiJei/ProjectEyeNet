package com.eyenet.repository;

import com.eyenet.model.document.PerformanceMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PerformanceMetricsRepository extends MongoRepository<PerformanceMetrics, String> {
    List<PerformanceMetrics> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
