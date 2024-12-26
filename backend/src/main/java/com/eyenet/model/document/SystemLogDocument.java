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
@Document(collection = "system_logs")
public class SystemLogDocument {
    @Id
    private UUID id;
    private String level;
    private String message;
    private String source;
    private String type;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
    private String username;
    private String ipAddress;
    private UUID relatedEntityId;
    private String relatedEntityType;
    private LocalDateTime createdAt;
}
