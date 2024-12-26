package com.eyenet.repository;

import com.eyenet.model.document.UserDeviceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends MongoRepository<UserDeviceDocument, UUID> {
    List<UserDeviceDocument> findByUserIdAndEnabled(UUID userId, boolean enabled);
}
