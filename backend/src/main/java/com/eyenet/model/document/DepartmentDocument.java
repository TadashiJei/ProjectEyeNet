package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDocument {
    @Id
    private UUID id;

    private String name;

    private Long bandwidthQuota;

    private Integer priority;

    private NetworkRestrictions networkRestrictions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
