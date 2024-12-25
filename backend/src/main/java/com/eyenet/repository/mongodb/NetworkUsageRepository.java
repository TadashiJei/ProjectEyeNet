package com.eyenet.repository.mongodb;

import com.eyenet.model.document.NetworkUsage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NetworkUsageRepository extends MongoRepository<NetworkUsage, String> {
    List<NetworkUsage> findByDepartmentId(UUID departmentId);
    
    List<NetworkUsage> findByDeviceId(String deviceId);
    
    List<NetworkUsage> findByDepartmentIdAndTimestampBetween(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'departmentId': ?0, 'bytesIn': {$gt: ?1}}")
    List<NetworkUsage> findHighBandwidthUsage(UUID departmentId, long threshold);
    
    @Query(value = "{'timestamp': {$gte: ?0, $lt: ?1}}", 
           sort = "{'bytesIn': -1, 'bytesOut': -1}")
    List<NetworkUsage> findTopUsageInTimeRange(LocalDateTime start, LocalDateTime end);
}
