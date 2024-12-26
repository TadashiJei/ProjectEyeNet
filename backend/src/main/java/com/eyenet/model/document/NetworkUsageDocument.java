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
@Document(collection = "network_usage")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkUsageDocument {
    @Id
    private UUID id;

    @Field("device_id")
    @Indexed
    private UUID deviceId;

    @Field("user_id")
    @Indexed
    private UUID userId;

    @Field("department_id")
    @Indexed
    private UUID departmentId;

    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;

    @Field("bytes_in")
    private Long bytesIn;

    @Field("bytes_out")
    private Long bytesOut;

    @Field("packets_in")
    private Long packetsIn;

    @Field("packets_out")
    private Long packetsOut;

    @Field("active_connections")
    private Integer activeConnections;

    @Field("bandwidth_usage")
    private Double bandwidthUsage;

    @Field("protocol_stats")
    private Map<String, Long> protocolStats;

    @Field("application_stats")
    private Map<String, Long> applicationStats;

    @Field("latency")
    private Double latency;

    @Field("packet_loss")
    private Double packetLoss;

    @Field("jitter")
    private Double jitter;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;
}
