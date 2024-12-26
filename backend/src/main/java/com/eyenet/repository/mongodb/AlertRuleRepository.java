package com.eyenet.repository.mongodb;

import com.eyenet.model.document.AlertRuleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRuleRepository extends MongoRepository<AlertRuleDocument, UUID> {
    List<AlertRuleDocument> findByEnabled(boolean enabled);
    
    List<AlertRuleDocument> findByDeviceId(UUID deviceId);
    
    List<AlertRuleDocument> findByType(String type);
    
    List<AlertRuleDocument> findBySeverity(String severity);
    
    List<AlertRuleDocument> findByDeviceIdAndEnabled(UUID deviceId, boolean enabled);
    
    @Query("{'deviceId': ?0, 'type': ?1, 'enabled': true}")
    List<AlertRuleDocument> findActiveRulesByType(UUID deviceId, String type);
    
    @Query("{'deviceId': ?0, 'severity': ?1, 'enabled': true}")
    List<AlertRuleDocument> findActiveRulesBySeverity(UUID deviceId, String severity);

    List<AlertRuleDocument> findByDepartmentId(UUID departmentId);

    List<AlertRuleDocument> findByDepartmentIdAndEnabled(UUID departmentId, boolean enabled);

    List<AlertRuleDocument> findByDeviceIdAndType(UUID deviceId, String type);

    List<AlertRuleDocument> findByDeviceIdAndSeverity(UUID deviceId, String severity);

    List<AlertRuleDocument> findByDeviceIdAndTypeAndEnabled(UUID deviceId, String type, boolean enabled);

    List<AlertRuleDocument> findByDeviceIdAndSeverityAndEnabled(UUID deviceId, String severity, boolean enabled);
}
