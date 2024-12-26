package com.eyenet.repository.mongodb;

import com.eyenet.model.document.NetworkConfigDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NetworkConfigurationRepository extends MongoRepository<NetworkConfigDocument, UUID> {
    // Add custom query methods as needed
}
