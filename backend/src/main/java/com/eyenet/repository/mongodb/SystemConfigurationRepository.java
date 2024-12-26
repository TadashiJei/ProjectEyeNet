package com.eyenet.repository.mongodb;

import com.eyenet.model.document.SystemConfigurationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SystemConfigurationRepository extends MongoRepository<SystemConfigurationDocument, UUID> {
    Optional<SystemConfigurationDocument> findByVersion(String version);
    List<SystemConfigurationDocument> findByStatus(String status);
    List<SystemConfigurationDocument> findByLastUpdatedBetween(LocalDateTime start, LocalDateTime end);
    List<SystemConfigurationDocument> findByUpdatedBy(String updatedBy);
}
