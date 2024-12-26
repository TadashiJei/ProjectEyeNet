package com.eyenet.repository.mongodb;

import com.eyenet.model.document.RoleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends MongoRepository<RoleDocument, UUID> {
    Optional<RoleDocument> findByName(String name);
    
    List<RoleDocument> findByEnabled(boolean enabled);
    
    @Query("{'permissions': ?0}")
    List<RoleDocument> findByPermission(String permission);
    
    @Query("{'departmentId': ?0}")
    List<RoleDocument> findByDepartment(UUID departmentId);
    
    @Query("{'departmentId': ?0, 'enabled': true}")
    List<RoleDocument> findActiveDepartmentRoles(UUID departmentId);
    
    List<RoleDocument> findByNameIn(Set<String> names);
    
    boolean existsByName(String name);
}
