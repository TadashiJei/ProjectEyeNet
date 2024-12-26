package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserProfileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfileDocument, UUID> {
    Optional<UserProfileDocument> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
