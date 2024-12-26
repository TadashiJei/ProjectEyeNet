package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, UUID> {
    Optional<UserDocument> findByUsername(String username);
    Optional<UserDocument> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
