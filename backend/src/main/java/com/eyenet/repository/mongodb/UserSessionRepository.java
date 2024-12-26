package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserSessionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends MongoRepository<UserSessionDocument, UUID> {
    List<UserSessionDocument> findByUserIdAndActive(UUID userId, boolean active);
    
    Optional<UserSessionDocument> findBySessionToken(String sessionToken);
    
    void deleteBySessionToken(String sessionToken);
    
    List<UserSessionDocument> findByUserIdOrderByLastActivityAtDesc(UUID userId);
    
    @Query("{'userId': ?0, 'active': true, 'sessionToken': {$ne: ?1}}")
    List<UserSessionDocument> findOtherActiveSessions(UUID userId, String currentSessionToken);
    
    @Query("{'userId': ?0, 'active': true, 'sessionToken': {$ne: ?1}}")
    @Transactional
    void terminateAllExcept(UUID userId, String sessionToken);
    
    @Query("{'expiresAt': {$lt: ?0}}")
    List<UserSessionDocument> findExpiredSessions(LocalDateTime now);
}
