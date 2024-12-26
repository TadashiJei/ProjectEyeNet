package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserActivityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends MongoRepository<UserActivityDocument, UUID> {
    List<UserActivityDocument> findByUserId(UUID userId);
    
    List<UserActivityDocument> findByUserIdAndType(UUID userId, String type);
    
    List<UserActivityDocument> findByUserIdOrderByTimestampDesc(UUID userId);
    
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<UserActivityDocument> findByUserIdAndTimeRange(
            UUID userId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'userId': ?0, 'type': ?1, 'timestamp': {$gte: ?2, $lt: ?3}}")
    List<UserActivityDocument> findByUserIdAndTypeAndTimeRange(
            UUID userId, String type, LocalDateTime start, LocalDateTime end);
    
    @Query("{'departmentId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<UserActivityDocument> findByDepartmentIdAndTimeRange(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
}
