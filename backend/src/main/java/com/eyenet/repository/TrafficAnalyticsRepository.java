package com.eyenet.repository;

import com.eyenet.model.document.TrafficAnalyticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrafficAnalyticsRepository extends MongoRepository<TrafficAnalyticsDocument, UUID> {
}
