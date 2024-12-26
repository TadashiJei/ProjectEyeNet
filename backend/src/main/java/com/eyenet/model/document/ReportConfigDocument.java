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
@Document(collection = "report_configs")
public class ReportConfigDocument {
    @Id
    private UUID id;
    private String type;
    private UUID departmentId;
    private String requestedBy;
    private Map<String, String> parameters;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
