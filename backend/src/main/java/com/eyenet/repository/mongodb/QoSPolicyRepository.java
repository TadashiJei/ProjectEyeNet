package com.eyenet.repository.mongodb;

import com.eyenet.model.document.QoSPolicyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QoSPolicyRepository extends MongoRepository<QoSPolicyDocument, UUID> {
    // Add custom query methods as needed
}
