package com.eyenet.repository.mongodb;

import com.eyenet.model.document.FlowRuleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlowRuleRepository extends MongoRepository<FlowRuleDocument, UUID> {
    // Add custom query methods as needed
}
