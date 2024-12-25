package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDocument {
    @Id
    private String id;

    private String name;

    private Long bandwidthQuota;

    private Integer priority;

    private NetworkRestrictions networkRestrictions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NetworkRestrictions {
        private Long maxBandwidth;
        private Long dailyDataLimit;
        private Boolean socialMediaBlocked;
        private Boolean streamingBlocked;
    }
}
