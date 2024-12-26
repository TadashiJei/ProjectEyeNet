package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "report_schedules")
public class ReportScheduleDocument {
    @Id
    private UUID id;
    private UUID departmentId;
    private String name;
    private String description;
    private String cronExpression;
    private String reportType;
    private String format;
    private String recipients;
    private boolean enabled;
    private LocalDateTime lastRun;
    private LocalDateTime nextRun;
    private String status;
}
