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
@Document(collection = "alerts")
public class AlertDocument {
    public enum AlertStatus {
        NEW, ACKNOWLEDGED, RESOLVED, CLOSED
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    @Id
    private UUID id;
    private UUID deviceId;
    private UUID departmentId;
    private String title;
    private String message;
    private Severity severity;
    private AlertStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID assignedTo;
    private String resolution;
}
