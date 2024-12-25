package com.eyenet.repository.mongodb;

import com.eyenet.model.document.TrafficAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrafficAnalyticsRepository extends MongoRepository<TrafficAnalytics, String> {
    List<TrafficAnalytics> findByDepartmentId(UUID departmentId);
    
    List<TrafficAnalytics> findByDepartmentIdAndTimestampBetween(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'departmentId': ?0, 'bandwidthUsageMbps': {$gt: ?1}}")
    List<TrafficAnalytics> findHighBandwidthPeriods(UUID departmentId, double threshold);
    
    @Query(value = "{'timestamp': {$gte: ?0, $lt: ?1}}", 
           sort = "{'peakBandwidthMbps': -1}")
    List<TrafficAnalytics> findPeakTrafficPeriods(LocalDateTime start, LocalDateTime end);
    
    @Query(value = "{'departmentId': ?0}", 
           sort = "{'timestamp': -1}")
    List<TrafficAnalytics> findLatestAnalytics(UUID departmentId, int limit);
}
