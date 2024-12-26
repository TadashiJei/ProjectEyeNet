package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "security_metrics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityMetrics {
    @Id
    private UUID id;
    private LocalDateTime timestamp;
    private int totalAlerts;
    private int criticalAlerts;
    private int highAlerts;
    private int mediumAlerts;
    private int lowAlerts;
    private List<String> topThreats;
    private List<String> affectedSystems;
    private double threatScore;
    private String securityStatus;
}
