package com.eyenet.repository.mongodb;

import com.eyenet.model.document.FlowRuleTemplateDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlowRuleTemplateRepository extends MongoRepository<FlowRuleTemplateDocument, UUID> {
    // Add custom query methods as needed
}
