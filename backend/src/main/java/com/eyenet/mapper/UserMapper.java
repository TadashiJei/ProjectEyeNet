package com.eyenet.mapper;

import com.eyenet.model.document.UserDocument;
import com.eyenet.model.entity.User;
import com.eyenet.model.entity.Role;
import com.eyenet.model.dto.UserProfileDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    public UserDocument toDocument(User user) {
        if (user == null) {
            return null;
        }
        
        return UserDocument.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .active(user.isActive())
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    public User toEntity(UserDocument document) {
        if (document == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(document.getUsername());
        user.setEmail(document.getEmail());
        user.setPassword(document.getPassword());
        user.setActive(document.isEnabled());
        user.setLastLogin(document.getLastLogin());
        user.setCreatedAt(document.getCreatedAt());
        user.setUpdatedAt(document.getUpdatedAt());
        
        // Convert string roles to Role entities
        Set<Role> roles = document.getRoles().stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toSet());
        user.setRoles(roles);
        
        return user;
    }
    
    public UserProfileDTO mapToUserProfileDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDepartment(user.getDepartment());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        dto.setEnabled(user.isEnabled());
        dto.setLastLogin(user.getLastLogin());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setJobTitle(user.getJobTitle());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
