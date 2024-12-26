package com.eyenet.repository;

import com.eyenet.model.document.UserNotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserNotificationRepository extends MongoRepository<UserNotificationDocument, UUID> {
    List<UserNotificationDocument> findByUserIdOrderByCreatedAtDesc(UUID userId);

    @Query("{ 'userId': ?0, 'read': false }")
    @Update("{ '$set': { 'read': true, 'readAt': ?1 } }")
    void markAllAsRead(UUID userId, LocalDateTime readAt);
}
