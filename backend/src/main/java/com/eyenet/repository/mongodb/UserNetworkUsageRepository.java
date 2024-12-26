package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserNetworkUsageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserNetworkUsageRepository extends MongoRepository<UserNetworkUsageDocument, UUID> {
    Optional<UserNetworkUsageDocument> findByUserId(UUID userId);
    
    List<UserNetworkUsageDocument> findByUserIdAndTimestampBetween(
            UUID userId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'userId': ?0, 'bytesTransferred': {$gt: ?1}}")
    List<UserNetworkUsageDocument> findHighUsage(UUID userId, Long threshold);
    
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1}, 'bytesTransferred': {$gt: ?2}}")
    List<UserNetworkUsageDocument> findRecentHighUsage(
            UUID userId, LocalDateTime since, Long threshold);
    
    @Query("{'departmentId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<UserNetworkUsageDocument> findDepartmentUsage(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
}
