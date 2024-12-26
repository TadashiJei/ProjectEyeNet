package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserDeviceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDeviceDocumentRepository extends MongoRepository<UserDeviceDocument, UUID> {
    List<UserDeviceDocument> findByUserId(UUID userId);
    Optional<UserDeviceDocument> findByDeviceId(String deviceId);
    void deleteByUserId(UUID userId);
    boolean existsByDeviceId(String deviceId);
}
