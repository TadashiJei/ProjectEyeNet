package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "performance_metrics")
public class PerformanceMetricsDocument {
    @Id
    private UUID id;

    @DBRef
    @Field("device")
    private NetworkDeviceDocument device;

    @Field("device_id")
    private UUID deviceId;

    @Field("department_id")
    private UUID departmentId;

    @Field("cpu_usage")
    private Double cpuUsage;

    @Field("memory_usage")
    private Double memoryUsage;

    @Field("disk_usage")
    private Double diskUsage;

    @Field("network_throughput")
    private Double networkThroughput;

    @Field("latency")
    private Double latency;

    @Field("packet_loss")
    private Double packetLoss;

    @Field("error_rate")
    private Double errorRate;

    @Field("active_connections")
    private Integer activeConnections;

    @Field("bandwidth_utilization")
    private Double bandwidthUtilization;

    @Field("queue_depth")
    private Integer queueDepth;

    @Field("buffer_usage")
    private Double bufferUsage;

    @Field("metadata")
    private Map<String, String> metadata;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("created_by")
    private UUID createdBy;

    @Field("updated_by")
    private UUID updatedBy;

    public Double getNetworkThroughput() {
        return networkThroughput;
    }

    public Double getLatency() {
        return latency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setNetworkThroughput(Double networkThroughput) {
        this.networkThroughput = networkThroughput;
    }

    public void setLatency(Double latency) {
        this.latency = latency;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
