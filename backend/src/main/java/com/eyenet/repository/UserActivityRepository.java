package com.eyenet.repository;

import com.eyenet.model.document.UserActivityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends MongoRepository<UserActivityDocument, UUID> {
    List<UserActivityDocument> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
