package com.eyenet.repository;

import com.eyenet.model.document.UserPreferencesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPreferencesRepository extends MongoRepository<UserPreferencesDocument, UUID> {
    Optional<UserPreferencesDocument> findByUserId(UUID userId);
}
