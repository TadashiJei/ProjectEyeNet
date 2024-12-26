package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "report_schedule_configs")
public class ReportScheduleConfigDocument {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String cronExpression;
    private String reportType;
    private UUID departmentId;
    private Map<String, String> parameters;
    private String status;
    private LocalDateTime lastRunTime;
    private LocalDateTime nextRunTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
