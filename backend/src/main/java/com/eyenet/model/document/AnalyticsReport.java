package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "analytics_reports")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsReport {
    @Id
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime generatedAt;
    private String reportType;
    private String format;
    private String content;
    private ReportConfig config;
}
