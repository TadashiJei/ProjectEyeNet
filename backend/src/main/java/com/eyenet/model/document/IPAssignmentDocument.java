package com.eyenet.model.document;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ip_assignments")
public class IPAssignmentDocument {
    @Id
    private UUID id;
    private String ipAddress;
    private UUID deviceId;
    private UUID rangeId;
    private LocalDateTime assignedAt;
    private LocalDateTime expiresAt;
    private boolean isDynamic;
    private String status;
}
