package com.eyenet.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class NetworkUsageStats {
    private long totalBytesTransferred;
    private long uploadBytes;
    private long downloadBytes;
    private int activeConnections;
    private Map<String, Long> protocolUsage;
    private Map<String, Long> applicationUsage;
    private Map<LocalDateTime, Long> usageOverTime;
    private double averageBandwidth;
    private double peakBandwidth;
    private LocalDateTime peakUsageTime;
}
