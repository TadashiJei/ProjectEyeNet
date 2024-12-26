package com.eyenet.repository.mongodb;

import com.eyenet.model.document.WebsiteAccessLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WebsiteAccessLogRepository extends MongoRepository<WebsiteAccessLogDocument, UUID> {
    List<WebsiteAccessLogDocument> findByUserId(UUID userId);
    
    List<WebsiteAccessLogDocument> findByDepartmentId(UUID departmentId);
    
    List<WebsiteAccessLogDocument> findByUrl(String url);
    
    @Query("{'statusCode': {$gte: ?0}}")
    List<WebsiteAccessLogDocument> findByStatusCodeGreaterThanEqual(Integer statusCode);
    
    @Query("{'responseTime': {$gt: ?0}}")
    List<WebsiteAccessLogDocument> findSlowResponses(Long threshold);
    
    @Query("{'bytesTransferred': {$gt: ?0}}")
    List<WebsiteAccessLogDocument> findLargeTransfers(Long threshold);
    
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<WebsiteAccessLogDocument> findUserActivityBetween(
            UUID userId, LocalDateTime start, LocalDateTime end);
    
    @Query("{'departmentId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<WebsiteAccessLogDocument> findDepartmentActivityBetween(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
}
