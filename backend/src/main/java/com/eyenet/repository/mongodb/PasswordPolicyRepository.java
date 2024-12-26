package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PasswordPolicyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordPolicyRepository extends MongoRepository<PasswordPolicyDocument, UUID> {
    // Add custom query methods as needed
}
