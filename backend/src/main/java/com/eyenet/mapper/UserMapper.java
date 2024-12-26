package com.eyenet.mapper;

import com.eyenet.model.document.UserDocument;
import com.eyenet.model.dto.UserProfileDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
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
                .profilePicture(user.getProfilePicture())
                .departmentId(user.getDepartmentId())
                .roles(user.getRoles())
                .active(user.isEnabled())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
