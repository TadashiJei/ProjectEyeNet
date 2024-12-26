package com.eyenet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkUsageStats {
    private long totalBytesIn;
    private long totalBytesOut;
    private double averageBandwidth;
    private double peakBandwidth;
    private long totalConnections;
}
