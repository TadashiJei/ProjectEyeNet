package com.eyenet.repository.mongodb;

import com.eyenet.model.document.NetworkDeviceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NetworkDeviceRepository extends MongoRepository<NetworkDeviceDocument, UUID> {
    // Add custom query methods as needed
}
