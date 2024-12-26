package com.eyenet.repository.mongodb;

import com.eyenet.model.document.SystemLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SystemLogRepository extends MongoRepository<SystemLogDocument, UUID> {
    List<SystemLogDocument> findByLevel(String level);
    
    List<SystemLogDocument> findBySource(String source);
    
    @Query("{'timestamp': {$gte: ?0, $lt: ?1}}")
    List<SystemLogDocument> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{'level': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<SystemLogDocument> findByLevelAndTimestampBetween(
            String level, LocalDateTime start, LocalDateTime end);
    
    @Query("{'source': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<SystemLogDocument> findBySourceAndTimestampBetween(
            String source, LocalDateTime start, LocalDateTime end);
    
    List<SystemLogDocument> findByLevelAndSource(String level, String source);
    
    @Query("{'level': {$in: ['ERROR', 'FATAL']}}")
    List<SystemLogDocument> findCriticalLogs();
    
    void deleteByTimestampBefore(LocalDateTime timestamp);
}
