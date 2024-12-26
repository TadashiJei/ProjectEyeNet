package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "report_configs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportConfig {
    @Id
    private UUID id;
    private String reportType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> metrics;
    private String format;
    private boolean includeCharts;
    private boolean includeTables;
    private String aggregationLevel;
}
