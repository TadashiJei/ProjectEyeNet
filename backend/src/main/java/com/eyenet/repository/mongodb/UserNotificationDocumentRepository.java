package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserNotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserNotificationDocumentRepository extends MongoRepository<UserNotificationDocument, UUID> {
    List<UserNotificationDocument> findByUserId(UUID userId);
    List<UserNotificationDocument> findByUserIdAndRead(UUID userId, boolean read);
    List<UserNotificationDocument> findByUserIdAndTimestampAfter(UUID userId, LocalDateTime timestamp);
    void deleteByUserId(UUID userId);
}
