package com.eyenet.repository;

import com.eyenet.model.document.SecurityMetricsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecurityMetricsRepository extends MongoRepository<SecurityMetricsDocument, UUID> {
}
