package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "network_config")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkConfigDocument {
    @Id
    private String id;

    @Indexed(unique = true)
    private String switchId;

    private List<FlowRule> flowRules;

    private QoSConfig qosConfig;

    private Map<String, String> properties;

    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlowRule {
        private String ruleId;
        private Integer priority;
        private String matchCriteria;
        private String action;
        private Boolean isActive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QoSConfig {
        private Integer maxBandwidth;
        private Integer minBandwidth;
        private Integer priority;
        private String schedulingAlgorithm;
        private Map<String, Integer> portPriorities;
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
