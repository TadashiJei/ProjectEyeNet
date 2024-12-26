package com.eyenet.repository.mongodb;

import com.eyenet.model.document.FlowRuleDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlowRuleRepository extends MongoRepository<FlowRuleDocument, UUID> {
    Page<FlowRuleDocument> findByDeviceId(UUID deviceId, Pageable pageable);
    Page<FlowRuleDocument> findByTableId(Integer tableId, Pageable pageable);
    Page<FlowRuleDocument> findByPriority(Integer priority, Pageable pageable);
    Page<FlowRuleDocument> findByEnabled(boolean enabled, Pageable pageable);
    Page<FlowRuleDocument> findByStatus(FlowRuleDocument.FlowRuleStatus status, Pageable pageable);
    Page<FlowRuleDocument> findByDeviceIdAndTableId(UUID deviceId, Integer tableId, Pageable pageable);
    Page<FlowRuleDocument> findByDeviceIdAndEnabled(UUID deviceId, boolean enabled, Pageable pageable);
    Page<FlowRuleDocument> findByDeviceIdAndStatus(UUID deviceId, FlowRuleDocument.FlowRuleStatus status, Pageable pageable);
    
    List<FlowRuleDocument> findByDeviceIdAndTableIdAndEnabled(UUID deviceId, Integer tableId, boolean enabled);
    List<FlowRuleDocument> findByDeviceIdAndStatusAndEnabled(UUID deviceId, FlowRuleDocument.FlowRuleStatus status, boolean enabled);
    
    long countByDeviceIdAndEnabled(UUID deviceId, boolean enabled);
    long countByDeviceIdAndStatus(UUID deviceId, FlowRuleDocument.FlowRuleStatus status);
}
