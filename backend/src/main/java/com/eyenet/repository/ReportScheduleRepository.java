package com.eyenet.repository;

import com.eyenet.model.document.ReportScheduleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportScheduleRepository extends MongoRepository<ReportScheduleDocument, UUID> {
}
