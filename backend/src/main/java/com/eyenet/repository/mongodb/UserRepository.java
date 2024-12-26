package com.eyenet.repository.mongodb;

import com.eyenet.model.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, UUID> {
    Optional<UserDocument> findByUsername(String username);
    
    Optional<UserDocument> findByEmail(String email);
    
    List<UserDocument> findByDepartmentId(UUID departmentId);
    
    List<UserDocument> findByEnabled(boolean enabled);
    
    @Query("{'roles': ?0}")
    List<UserDocument> findByRole(String role);
    
    @Query("{'lastLoginAt': {$gte: ?0, $lt: ?1}}")
    List<UserDocument> findByLastLoginBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{'departmentId': ?0, 'enabled': true}")
    List<UserDocument> findActiveUsersByDepartment(UUID departmentId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
