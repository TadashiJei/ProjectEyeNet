package com.eyenet.repository.mongodb;

import com.eyenet.model.document.PermissionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PermissionRepository extends MongoRepository<PermissionDocument, UUID> {
    Optional<PermissionDocument> findByName(String name);
    
    List<PermissionDocument> findByCategory(String category);
    
    List<PermissionDocument> findByEnabled(boolean enabled);
    
    @Query("{'departmentId': ?0}")
    List<PermissionDocument> findByDepartment(UUID departmentId);
    
    @Query("{'departmentId': ?0, 'enabled': true}")
    List<PermissionDocument> findActiveDepartmentPermissions(UUID departmentId);
    
    List<PermissionDocument> findByNameIn(Set<String> names);
    
    boolean existsByName(String name);
}
