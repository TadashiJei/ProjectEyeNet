package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PasswordResetDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends MongoRepository<PasswordResetDocument, UUID> {
    Optional<PasswordResetDocument> findByTokenAndUsedFalse(String token);
    Optional<PasswordResetDocument> findByUserIdAndUsedFalse(UUID userId);
}
