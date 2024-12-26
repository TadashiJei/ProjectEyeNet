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
@Document(collection = "system_configurations")
public class SystemConfigurationDocument {
    @Id
    private UUID id;
    private Integer maxConcurrentConnections;
    private Long maxBandwidthPerUser;
    private String defaultQoSPolicy;
    private Integer monitoringInterval;
    private Double alertThreshold;
    private Map<String, String> additionalSettings;
    private String status;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private String updatedBy;
    private String version;
}
