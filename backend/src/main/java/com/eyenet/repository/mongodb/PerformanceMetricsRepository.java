package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PerformanceMetricsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceMetricsRepository extends MongoRepository<PerformanceMetricsDocument, String> {
    List<PerformanceMetricsDocument> findByDeviceId(String deviceId);
    
    List<PerformanceMetricsDocument> findByDeviceIdAndTimestampBetween(
            String deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'deviceId': ?0, 'latency': {$gt: ?1}}")
    List<PerformanceMetricsDocument> findHighLatencyEvents(String deviceId, double threshold);
    
    @Query("{'deviceId': ?0, 'packetLoss': {$gt: ?1}}")
    List<PerformanceMetricsDocument> findHighPacketLossEvents(String deviceId, double threshold);
    
    @Query(value = "{'deviceId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}", 
           sort = "{'latency': -1, 'packetLoss': -1}")
    List<PerformanceMetricsDocument> findPerformanceIssues(String deviceId, 
            LocalDateTime start, LocalDateTime end);
}
