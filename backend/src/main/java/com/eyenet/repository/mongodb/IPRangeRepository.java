package com.eyenet.repository.mongodb;

import com.eyenet.model.document.IPRangeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IPRangeRepository extends MongoRepository<IPRangeDocument, UUID> {
    // Add custom query methods as needed
}
