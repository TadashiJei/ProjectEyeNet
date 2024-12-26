package com.eyenet.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardMetricsDTO {
    private double averageBandwidth;
    private double averageLatency;
    private long activeAlerts;
}
