package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserPreferencesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPreferencesDocumentRepository extends MongoRepository<UserPreferencesDocument, UUID> {
    Optional<UserPreferencesDocument> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
