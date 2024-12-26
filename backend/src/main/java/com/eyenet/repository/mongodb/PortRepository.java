package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PortDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PortRepository extends MongoRepository<PortDocument, UUID> {
    // Add custom query methods as needed
}
