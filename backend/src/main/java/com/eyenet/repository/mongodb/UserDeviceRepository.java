package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserDeviceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends MongoRepository<UserDeviceDocument, UUID> {
    List<UserDeviceDocument> findByUserId(UUID userId);
    
    Optional<UserDeviceDocument> findByUserIdAndId(UUID userId, UUID deviceId);
    
    Optional<UserDeviceDocument> findByDeviceIdentifier(String deviceIdentifier);
    
    @Query("{'userId': ?0, 'status': 'ACTIVE', 'terminated': false}")
    List<UserDeviceDocument> findActiveDevices(UUID userId);
    
    @Query("{'lastSeenAt': {$lt: ?0}}")
    List<UserDeviceDocument> findInactiveDevices(LocalDateTime threshold);
    
    @Query("{'userId': ?0, 'deviceType': ?1}")
    List<UserDeviceDocument> findByUserIdAndDeviceType(UUID userId, String deviceType);
    
    @Query("{'userId': ?0, 'trusted': true}")
    List<UserDeviceDocument> findTrustedDevices(UUID userId);
}
