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
@Document(collection = "password_history")
public class PasswordHistoryDocument {
    @Id
    private UUID id;
    private UUID userId;
    private String passwordHash;
    private LocalDateTime changedAt;
    private String changedBy;
    private String reason;
}
