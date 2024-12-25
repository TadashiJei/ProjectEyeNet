package com.eyenet.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class BandwidthTrend {
    private LocalDateTime timestamp;
    private double totalBandwidth;
    private double inboundBandwidth;
    private double outboundBandwidth;
    private Map<String, Double> applicationBandwidth;
    private Map<String, Double> protocolBandwidth;
    private int utilizationPercentage;
    private double peakUsage;
    private double averageUsage;
}
