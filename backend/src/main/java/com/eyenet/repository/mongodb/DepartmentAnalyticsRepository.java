package com.eyenet.repository.mongodb;

import com.eyenet.model.document.DepartmentAnalyticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentAnalyticsRepository extends MongoRepository<DepartmentAnalyticsDocument, UUID> {
    List<DepartmentAnalyticsDocument> findByDepartmentId(UUID departmentId);
    
    List<DepartmentAnalyticsDocument> findByDepartmentIdAndTimestampBetween(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'departmentId': ?0, 'bandwidthUsage.utilizationPercentage': {$gt: ?1}}")
    List<DepartmentAnalyticsDocument> findHighBandwidthUtilization(UUID departmentId, Double threshold);
    
    @Query("{'departmentId': ?0, 'securityMetrics.riskScore': {$gt: ?1}}")
    List<DepartmentAnalyticsDocument> findHighRiskPeriods(UUID departmentId, Double threshold);
    
    @Query("{'departmentId': ?0, 'performanceMetrics.availability': {$lt: ?1}}")
    List<DepartmentAnalyticsDocument> findLowAvailabilityPeriods(UUID departmentId, Double threshold);
    
    @Query("{'departmentId': ?0, 'performanceMetrics.errorCount': {$gt: ?1}}")
    List<DepartmentAnalyticsDocument> findHighErrorPeriods(UUID departmentId, Integer threshold);
    
    @Query(value = "{'departmentId': ?0}", sort = "{'timestamp': -1}")
    List<DepartmentAnalyticsDocument> findLatestAnalytics(UUID departmentId);
}
