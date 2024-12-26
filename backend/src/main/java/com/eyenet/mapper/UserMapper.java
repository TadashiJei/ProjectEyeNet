package com.eyenet.mapper;

import com.eyenet.model.document.UserDocument;
import com.eyenet.model.dto.UserProfileDTO;
import com.eyenet.model.entity.User;
import com.eyenet.model.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    public UserProfileDTO toProfileDTO(UserDocument user) {
        if (user == null) {
            return null;
        }
        
        return UserProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .jobTitle(user.getJobTitle())
                .departmentId(user.getDepartmentId())
                .departmentName(user.getDepartmentName())
                .profilePicture(user.getProfilePicture())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .timezone(user.getTimezone())
                .language(user.getLanguage())
                .theme(user.getTheme())
                .mfaEnabled(user.isMfaEnabled())
                .emailVerified(user.isEmailVerified())
                .phoneVerified(user.isPhoneVerified())
                .build();
    }

    public UserDocument mapToUserDocument(User user) {
        if (user == null) {
            return null;
        }

        return UserDocument.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .jobTitle(user.getJobTitle())
                .departmentId(user.getDepartmentId())
                .departmentName(user.getDepartmentName())
                .profilePicture(user.getProfilePicture())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .enabled(user.isEnabled())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .timezone(user.getTimezone())
                .language(user.getLanguage())
                .theme(user.getTheme())
                .mfaEnabled(user.isMfaEnabled())
                .emailVerified(user.isEmailVerified())
                .phoneVerified(user.isPhoneVerified())
                .build();
    }

    public User mapToUser(UserDocument userDocument) {
        if (userDocument == null) {
            return null;
        }

        User user = new User();
        user.setId(userDocument.getId());
        user.setUsername(userDocument.getUsername());
        user.setPassword(userDocument.getPassword());
        user.setEmail(userDocument.getEmail());
        user.setFirstName(userDocument.getFirstName());
        user.setLastName(userDocument.getLastName());
        user.setPhoneNumber(userDocument.getPhoneNumber());
        user.setJobTitle(userDocument.getJobTitle());
        user.setProfilePicture(userDocument.getProfilePicture());
        user.setDepartmentId(userDocument.getDepartmentId());
        user.setDepartmentName(userDocument.getDepartmentName());
        user.setRoles(userDocument.getRoles().stream().map(roleName -> {
            Role role = new Role();
            role.setName(roleName);
            return role;
        }).collect(Collectors.toSet()));
        user.setEnabled(userDocument.isEnabled());
        user.setLastLoginAt(userDocument.getLastLoginAt());
        user.setCreatedAt(userDocument.getCreatedAt());
        user.setUpdatedAt(userDocument.getUpdatedAt());
        user.setTimezone(userDocument.getTimezone());
        user.setLanguage(userDocument.getLanguage());
        user.setTheme(userDocument.getTheme());
        user.setMfaEnabled(userDocument.isMfaEnabled());
        user.setEmailVerified(userDocument.isEmailVerified());
        user.setPhoneVerified(userDocument.isPhoneVerified());
        return user;
    }
}
