package com.eyenet.repository.mongodb;

import com.eyenet.model.document.DepartmentAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentAnalyticsRepository extends MongoRepository<DepartmentAnalytics, String> {
    List<DepartmentAnalytics> findByDepartmentId(UUID departmentId);
    
    List<DepartmentAnalytics> findByDepartmentIdAndIntervalType(UUID departmentId, String intervalType);
    
    List<DepartmentAnalytics> findByDepartmentIdAndTimestampBetween(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'departmentId': ?0, 'bandwidthUsage.quotaUtilizationPercent': {$gt: ?1}}")
    List<DepartmentAnalytics> findHighQuotaUtilization(UUID departmentId, double threshold);
    
    @Query("{'departmentId': ?0, 'securityMetrics.policyViolations': {$gt: 0}}")
    List<DepartmentAnalytics> findSecurityViolations(UUID departmentId);
    
    @Query(value = "{'departmentId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}", 
           sort = "{'bandwidthUsage.totalBytesTransferred': -1}")
    List<DepartmentAnalytics> findTopUsagePeriods(UUID departmentId, 
            LocalDateTime start, LocalDateTime end);
    
    @Query("{'complianceMetrics.quotaCompliant': false}")
    List<DepartmentAnalytics> findNonCompliantDepartments();
}
