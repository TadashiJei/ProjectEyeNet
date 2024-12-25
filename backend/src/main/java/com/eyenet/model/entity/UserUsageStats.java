package com.eyenet.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class UserUsageStats {
    private UUID userId;
    private String username;
    private long totalBytesTransferred;
    private long uploadBytes;
    private long downloadBytes;
    private int averageConnections;
    private Map<String, Long> topApplications;
    private Map<String, Long> topProtocols;
    private double averageBandwidth;
    private LocalDateTime lastActivity;
    private int bandwidthUtilization;
}
