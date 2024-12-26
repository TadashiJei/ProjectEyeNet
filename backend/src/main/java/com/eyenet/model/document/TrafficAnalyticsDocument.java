package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "traffic_analytics")
public class TrafficAnalyticsDocument {
    private UUID id;
    private UUID departmentId;
    private LocalDateTime timestamp;
    private long totalRequests;
    private long uniqueUsers;
    private double averageResponseTime;
    private double errorRate;
    private long bytesTransferred;
    private long packetsProcessed;
    private long activeConnections;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
