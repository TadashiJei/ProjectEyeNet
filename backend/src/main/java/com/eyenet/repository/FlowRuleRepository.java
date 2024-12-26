package com.eyenet.repository;

import com.eyenet.model.document.FlowRuleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowRuleRepository extends MongoRepository<FlowRuleDocument, String> {
    List<FlowRuleDocument> findByDeviceId(String deviceId);
    List<FlowRuleDocument> findByEnabled(boolean enabled);
}
