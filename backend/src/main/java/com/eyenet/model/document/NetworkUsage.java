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
public class NetworkUsage {
    @Id
    private String id;

    @Field("department_id")
    @Indexed
    private UUID departmentId;

    @Field("device_id")
    @Indexed
    private String deviceId;

    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;

    @Field("bytes_in")
    private long bytesIn;

    @Field("bytes_out")
    private long bytesOut;

    @Field("packets_in")
    private long packetsIn;

    @Field("packets_out")
    private long packetsOut;

    @Field("active_connections")
    private int activeConnections;

    @Field("protocols")
    private Map<String, Long> protocolUsage;

    @Field("applications")
    private Map<String, Long> applicationUsage;

    @Field("port_statistics")
    private Map<Integer, PortStats> portStatistics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PortStats {
        private long bytesIn;
        private long bytesOut;
        private long errors;
        private long drops;
    }
}
