package com.eyenet.repository;

import com.eyenet.model.document.UserSessionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends MongoRepository<UserSessionDocument, UUID> {
    List<UserSessionDocument> findByUserIdAndActiveOrderByCreatedAtDesc(UUID userId, boolean active);

    @Query("{ 'userId': ?0, '_id': { $ne: ?1 } }")
    void terminateAllExcept(UUID userId, UUID currentSessionId);
}
