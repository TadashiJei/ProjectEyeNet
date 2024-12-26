package com.eyenet.repository.mongodb;

import com.eyenet.model.document.TrafficAnalyticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrafficAnalyticsRepository extends MongoRepository<TrafficAnalyticsDocument, UUID> {
    List<TrafficAnalyticsDocument> findByDeviceId(UUID deviceId);
    
    List<TrafficAnalyticsDocument> findByDeviceIdAndTimestampBetween(
            UUID deviceId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'deviceId': ?0, 'bandwidth': {$gt: ?1}}")
    List<TrafficAnalyticsDocument> findHighBandwidthEvents(UUID deviceId, double threshold);
    
    @Query("{'deviceId': ?0, 'latency': {$gt: ?1}}")
    List<TrafficAnalyticsDocument> findHighLatencyEvents(UUID deviceId, double threshold);
    
    @Query("{'deviceId': ?0, 'packetLoss': {$gt: ?1}}")
    List<TrafficAnalyticsDocument> findHighPacketLossEvents(UUID deviceId, double threshold);
}
