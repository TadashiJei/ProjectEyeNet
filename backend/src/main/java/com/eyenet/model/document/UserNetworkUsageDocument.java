package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_network_usage")
public class UserNetworkUsageDocument {
    @Id
    private UUID id;
    private UUID userId;
    private UUID departmentId;
    private LocalDateTime timestamp;
    private long bytesIn;
    private long bytesOut;
    private long packetsIn;
    private long packetsOut;
    private Map<String, Double> bandwidthUsage;
    private Map<String, Double> applicationUsage;
    private Map<String, Long> protocolUsage;
}
