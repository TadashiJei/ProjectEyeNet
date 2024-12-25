package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "flow_rules")
public class FlowRule {
    @Id
    private String id;
    
    private String switchId;
    private Integer tableId;
    private Integer priority;
    private String matchCriteria; // JSON string containing match fields
    private String actions; // JSON string containing actions
    private Long idleTimeout;
    private Long hardTimeout;
    private Long cookie;
    private Boolean isPermanent;
    private Long byteCount;
    private Long packetCount;
    private Long duration;
}
