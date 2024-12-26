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
@Document(collection = "user_profiles")
public class UserProfileDocument {
    @Id
    private UUID id;
    private UUID userId;
    private String bio;
    private String avatarUrl;
    private String contactEmail;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String timezone;
    private String language;
    private String theme;
    private boolean emailNotifications;
    private boolean smsNotifications;
    private boolean pushNotifications;
    private LocalDateTime lastProfileUpdate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
