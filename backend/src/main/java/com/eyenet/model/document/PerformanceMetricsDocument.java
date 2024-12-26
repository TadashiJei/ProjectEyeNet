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
@Document(collection = "performance_metrics")
public class PerformanceMetricsDocument {
    @Id
    private UUID id;
    private UUID deviceId;
    private UUID departmentId;
    private LocalDateTime timestamp;
    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;
    private double networkLatency;
    private double packetLoss;
    private double throughput;
    private double errorRate;
    private double queueDepth;
    private String status;
}
