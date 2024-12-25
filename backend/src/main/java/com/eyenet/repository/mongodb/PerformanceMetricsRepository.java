package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PerformanceMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceMetricsRepository extends MongoRepository<PerformanceMetrics, String> {
    List<PerformanceMetrics> findByDeviceId(String deviceId);
    
    List<PerformanceMetrics> findByDeviceIdAndTimestampBetween(
            String deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'deviceId': ?0, 'latencyMs': {$gt: ?1}}")
    List<PerformanceMetrics> findHighLatencyEvents(String deviceId, double threshold);
    
    @Query("{'deviceId': ?0, 'packetLossPercent': {$gt: ?1}}")
    List<PerformanceMetrics> findHighPacketLossEvents(String deviceId, double threshold);
    
    @Query(value = "{'deviceId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}", 
           sort = "{'latencyMs': -1, 'packetLossPercent': -1}")
    List<PerformanceMetrics> findPerformanceIssues(String deviceId, 
            LocalDateTime start, LocalDateTime end);
}
