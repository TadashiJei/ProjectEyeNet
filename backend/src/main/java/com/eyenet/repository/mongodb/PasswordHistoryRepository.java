package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PasswordHistoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordHistoryRepository extends MongoRepository<PasswordHistoryDocument, UUID> {
    // Add custom query methods as needed
}
