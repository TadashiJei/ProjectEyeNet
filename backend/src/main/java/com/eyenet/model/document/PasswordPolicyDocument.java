package com.eyenet.model.document;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_policies")
public class PasswordPolicyDocument {
    @Id
    private UUID id;
    private int minLength;
    private int maxLength;
    private boolean requireUpperCase;
    private boolean requireLowerCase;
    private boolean requireNumbers;
    private boolean requireSpecialChars;
    private int maxAge;
    private int historySize;
    private int maxAttempts;
    private int lockoutDuration;
    private boolean isDefault;
    private UUID departmentId;
}
