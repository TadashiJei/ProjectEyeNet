package com.eyenet.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrafficAnalyticsDTO {
    private long totalRequests;
    private long uniqueUsers;
    private double averageResponseTime;
    private double errorRate;
}
