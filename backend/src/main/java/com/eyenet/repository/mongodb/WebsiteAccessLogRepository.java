package com.eyenet.repository.mongodb;

import com.eyenet.model.document.WebsiteAccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WebsiteAccessLogRepository extends MongoRepository<WebsiteAccessLog, String> {
    List<WebsiteAccessLog> findByDepartmentId(UUID departmentId);
    
    List<WebsiteAccessLog> findByUserId(UUID userId);
    
    List<WebsiteAccessLog> findByDepartmentIdAndTimestampBetween(
            UUID departmentId, LocalDateTime start, LocalDateTime end);
    
    List<WebsiteAccessLog> findByIsBlockedTrue();
    
    @Query("{'departmentId': ?0, 'category': ?1}")
    List<WebsiteAccessLog> findByCategoryAndDepartment(UUID departmentId, String category);
    
    @Query("{'departmentId': ?0, 'responseTimeMs': {$gt: ?1}}")
    List<WebsiteAccessLog> findSlowResponses(UUID departmentId, long threshold);
    
    @Query(value = "{'departmentId': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}", 
           sort = "{'bytesTransferred': -1}")
    List<WebsiteAccessLog> findTopTrafficSites(UUID departmentId, 
            LocalDateTime start, LocalDateTime end);
}
