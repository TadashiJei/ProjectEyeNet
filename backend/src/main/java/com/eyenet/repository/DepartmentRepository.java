package com.eyenet.repository;

import com.eyenet.model.document.DepartmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends MongoRepository<DepartmentDocument, UUID> {
}
