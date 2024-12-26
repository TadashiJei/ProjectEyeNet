package com.eyenet.repository;

import com.eyenet.model.document.WebsiteAccessLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WebsiteAccessLogDocumentRepository extends MongoRepository<WebsiteAccessLogDocument, UUID> {
    List<WebsiteAccessLogDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
    List<WebsiteAccessLogDocument> findByUserIdAndTimestampBetween(UUID userId, LocalDateTime start, LocalDateTime end);
}
