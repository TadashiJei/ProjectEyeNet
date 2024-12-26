package com.eyenet.repository.mongodb;

import com.eyenet.model.document.DepartmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends MongoRepository<DepartmentDocument, UUID> {
    Optional<DepartmentDocument> findByName(String name);
    
    List<DepartmentDocument> findByParentId(UUID parentId);
    
    List<DepartmentDocument> findByEnabled(boolean enabled);
    
    @Query("{'parentId': null}")
    List<DepartmentDocument> findRootDepartments();
    
    @Query("{'managers': ?0}")
    List<DepartmentDocument> findByManager(UUID managerId);
    
    @Query("{'members': ?0}")
    List<DepartmentDocument> findByMember(UUID memberId);
    
    boolean existsByName(String name);
}
