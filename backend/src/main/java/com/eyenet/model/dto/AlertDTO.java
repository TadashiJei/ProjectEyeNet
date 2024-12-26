package com.eyenet.model.dto;

import com.eyenet.model.document.AlertDocument;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AlertDTO {
    private UUID id;
    private String title;
    private String message;
    private AlertDocument.Severity severity;
    private AlertDocument.AlertStatus status;
    private LocalDateTime timestamp;
}
