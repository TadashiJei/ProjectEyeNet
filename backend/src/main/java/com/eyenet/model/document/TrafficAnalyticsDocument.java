package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "traffic_analytics")
public class TrafficAnalyticsDocument {
    @Id
    private UUID id;
    private UUID deviceId;
    private UUID departmentId;
    private LocalDateTime timestamp;
    private long bytesIn;
    private long bytesOut;
    private long packetsIn;
    private long packetsOut;
    private long flowCount;
    private double bandwidth;
    private double latency;
    private double packetLoss;
    private long totalRequests;
    private long uniqueUsers;
    private double averageResponseTime;
    private double errorRate;

    public long getTotalRequests() {
        return totalRequests;
    }

    public long getUniqueUsers() {
        return uniqueUsers;
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }

    public double getErrorRate() {
        return errorRate;
    }
}
