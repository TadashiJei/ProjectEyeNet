package com.eyenet.model.dto;

import com.eyenet.model.entity.Department;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UserProfileDTO {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Department department;
    private Set<String> roles;
    private boolean enabled;
    private LocalDateTime lastLogin;
    private String profilePicture;
    private String phoneNumber;
    private String jobTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
