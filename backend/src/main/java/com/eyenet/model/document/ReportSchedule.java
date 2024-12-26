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
@Document(collection = "report_schedules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportSchedule {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String cronExpression;
    private LocalDateTime nextRunTime;
    private LocalDateTime lastRunTime;
    private boolean active;
    private ReportConfig reportConfig;
}
