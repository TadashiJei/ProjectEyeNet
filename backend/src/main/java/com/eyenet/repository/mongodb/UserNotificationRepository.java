package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserNotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserNotificationRepository extends MongoRepository<UserNotificationDocument, UUID> {
    List<UserNotificationDocument> findByUserId(UUID userId);
    
    @Query("{'userId': ?0, 'read': false}")
    List<UserNotificationDocument> findUnreadNotifications(UUID userId);
    
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<UserNotificationDocument> findByUserIdAndTimeRange(
            UUID userId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'userId': ?0, 'type': ?1}")
    List<UserNotificationDocument> findByUserIdAndType(UUID userId, String type);
    
    @Query("{'userId': ?0, 'priority': ?1}")
    List<UserNotificationDocument> findByUserIdAndPriority(UUID userId, String priority);
    
    @Query("{'userId': ?0, 'read': false, 'timestamp': {$lt: ?1}}")
    List<UserNotificationDocument> findOldUnreadNotifications(UUID userId, LocalDateTime threshold);
    
    @Query("{'userId': ?0}")
    void deleteAllByUserId(UUID userId);
}
