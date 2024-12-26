package com.eyenet.repository;

import com.eyenet.model.document.AnalyticsReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnalyticsReportRepository extends MongoRepository<AnalyticsReport, UUID> {
}