package com.eyenet.repository;

import com.eyenet.model.document.UserNetworkUsageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserNetworkUsageRepository extends MongoRepository<UserNetworkUsageDocument, UUID> {
    @Query("{ 'userId': ?0 }")
    Optional<UserNetworkUsageDocument> findLatestByUserId(UUID userId);

    List<UserNetworkUsageDocument> findByDepartmentIdAndTimestampBetween(UUID departmentId, LocalDateTime start, LocalDateTime end);
}
