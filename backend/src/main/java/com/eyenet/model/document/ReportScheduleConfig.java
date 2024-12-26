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
@Document(collection = "report_schedule_configs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportScheduleConfig {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String cronExpression;
    private String reportType;
    private String format;
    private boolean includeCharts;
    private boolean includeTables;
    private String aggregationLevel;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
