package com.eyenet.model.document;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "qos_policies")
public class QoSPolicyDocument {
    @Id
    private UUID id;
    private String name;
    private String description;
    private int priority;
    private long bandwidthLimit;
    private int latencyTarget;
    private int packetLossThreshold;
    private String trafficType;
}
