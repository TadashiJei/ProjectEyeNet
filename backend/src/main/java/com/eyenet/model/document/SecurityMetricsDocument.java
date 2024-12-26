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
@Document(collection = "security_metrics")
public class SecurityMetricsDocument {
    @Id
    private UUID id;
    private UUID deviceId;
    private UUID departmentId;
    private LocalDateTime timestamp;
    private int threatLevel;
    private int anomalyCount;
    private int blockCount;
    private int allowCount;
    private int vulnerabilityCount;
    private int incidentCount;
    private String threatType;
    private String sourceIp;
    private String destinationIp;
    private String protocol;

    public int getVulnerabilityCount() {
        return vulnerabilityCount;
    }

    public int getIncidentCount() {
        return incidentCount;
    }

    public double getThreatLevel() {
        return threatLevel;
    }
}
