package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserSessionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionDocumentRepository extends MongoRepository<UserSessionDocument, UUID> {
    List<UserSessionDocument> findByUserId(UUID userId);
    List<UserSessionDocument> findByUserIdAndActive(UUID userId, boolean active);
    List<UserSessionDocument> findByUserIdAndLastActivityAfter(UUID userId, LocalDateTime timestamp);
    Optional<UserSessionDocument> findBySessionToken(String sessionToken);
    void deleteByUserId(UUID userId);
}
