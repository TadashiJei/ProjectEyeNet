package com.eyenet.model.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class ReportConfig {
    @NotNull
    private ReportType reportType;

    @NotNull
    private UUID departmentId;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    private Map<String, String> filters;
    private String format;
    private boolean includeCharts;
    private boolean includeSummary;

    public enum ReportType {
        NETWORK_USAGE,
        PERFORMANCE,
        SECURITY,
        WEBSITE_ACCESS,
        USER_ACTIVITY,
        BANDWIDTH,
        CUSTOM
    }
}
