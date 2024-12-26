package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "analytics_reports")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsReportDocument {
    @Id
    private UUID id;
    private UUID departmentId;
    private String reportType;
    private Map<String, Object> data;
    private LocalDateTime timestamp;
    private String status;
    private String generatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
