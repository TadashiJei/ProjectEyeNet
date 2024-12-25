package com.eyenet.model.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ReportScheduleConfig {
    @NotBlank(message = "Schedule name is required")
    private String name;

    @NotNull(message = "Report type is required")
    private ReportConfig.ReportType reportType;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @NotBlank(message = "Cron expression is required")
    private String cronExpression;

    private String emailRecipients;
    private boolean enabled = true;
}
