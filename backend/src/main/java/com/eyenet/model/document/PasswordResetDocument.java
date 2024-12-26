package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_resets")
public class PasswordResetDocument {
    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiryTime;
    private boolean used;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
