package com.eyenet.repository.mongodb;

import com.eyenet.model.document.NetworkUsageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NetworkUsageRepository extends MongoRepository<NetworkUsageDocument, UUID> {
    List<NetworkUsageDocument> findByDeviceId(UUID deviceId);
    
    List<NetworkUsageDocument> findByUserId(UUID userId);
    
    List<NetworkUsageDocument> findByDepartmentId(UUID departmentId);
    
    @Query("{'deviceId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<NetworkUsageDocument> findByDeviceIdAndTimestampBetween(
            UUID deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'deviceId': ?0, 'bandwidthUsage': {$gt: ?1}}")
    List<NetworkUsageDocument> findHighBandwidthUsage(UUID deviceId, Double threshold);
    
    @Query("{'deviceId': ?0, 'activeConnections': {$gt: ?1}}")
    List<NetworkUsageDocument> findHighConnectionCount(UUID deviceId, Integer threshold);
}
