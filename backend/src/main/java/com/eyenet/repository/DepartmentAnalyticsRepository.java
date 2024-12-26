package com.eyenet.repository;

import com.eyenet.model.document.DepartmentAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentAnalyticsRepository extends MongoRepository<DepartmentAnalytics, String> {
    List<DepartmentAnalytics> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
