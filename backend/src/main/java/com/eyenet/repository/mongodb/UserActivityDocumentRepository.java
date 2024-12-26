package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserActivityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityDocumentRepository extends MongoRepository<UserActivityDocument, UUID> {
    List<UserActivityDocument> findByUserId(UUID userId);
    List<UserActivityDocument> findByUserIdAndTimestampBetween(UUID userId, LocalDateTime start, LocalDateTime end);
    List<UserActivityDocument> findByUserIdAndActivityType(UUID userId, String activityType);
    void deleteByUserId(UUID userId);
}
