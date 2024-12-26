package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PasswordResetDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordResetRepository extends MongoRepository<PasswordResetDocument, UUID> {
    // Add custom query methods as needed
}
