package com.eyenet.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NetworkMetricsDTO {
    private double bandwidth;
    private double latency;
    private double packetLoss;
    private double jitter;
}
