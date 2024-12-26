package com.eyenet.repository;

import com.eyenet.model.document.WebsiteAccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WebsiteAccessLogRepository extends MongoRepository<WebsiteAccessLog, UUID> {
    List<WebsiteAccessLog> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
