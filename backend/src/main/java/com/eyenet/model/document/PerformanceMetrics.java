package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "performance_metrics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceMetrics {
    @Id
    private String id;

    @Field("device_id")
    @Indexed
    private String deviceId;

    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;

    @Field("cpu_usage")
    private Map<String, Double> cpuUsage;

    @Field("memory_usage")
    private MemoryMetrics memoryUsage;

    @Field("interface_metrics")
    private Map<String, InterfaceMetrics> interfaceMetrics;

    @Field("qos_metrics")
    private Map<String, QoSMetrics> qosMetrics;

    @Field("error_counts")
    private Map<String, Integer> errorCounts;

    @Field("latency_ms")
    private double latencyMs;

    @Field("jitter_ms")
    private double jitterMs;

    @Field("packet_loss_percent")
    private double packetLossPercent;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemoryMetrics {
        private long totalBytes;
        private long usedBytes;
        private long freeBytes;
        private double utilizationPercent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InterfaceMetrics {
        private double utilizationPercent;
        private long errorRate;
        private long discardRate;
        private double bandwidth;
        private QueueMetrics queueMetrics;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QoSMetrics {
        private String policyName;
        private long droppedPackets;
        private long markedPackets;
        private double averageQueueDepth;
        private double maxQueueDepth;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QueueMetrics {
        private int queueDepth;
        private int dropCount;
        private double averageDelay;
    }
}
