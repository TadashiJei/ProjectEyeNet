package com.eyenet.service;

import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDocument> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDocument getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserDocument createUser(UserDocument user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public UserDocument updateUser(UUID id, UserDocument updatedUser) {
        UserDocument user = getUserById(id);

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setJobTitle(updatedUser.getJobTitle());
        user.setDepartmentId(updatedUser.getDepartmentId());
        user.setDepartmentName(updatedUser.getDepartmentName());
        user.setProfilePicture(updatedUser.getProfilePicture());
        user.setEnabled(updatedUser.isEnabled());
        user.setTimezone(updatedUser.getTimezone());
        user.setLanguage(updatedUser.getLanguage());
        user.setTheme(updatedUser.getTheme());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        UserDocument user = getUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void hardDeleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public List<UserDocument> searchUsers(String username, String email, String role) {
        List<UserDocument> users = userRepository.findAll();
        
        return users.stream()
                .filter(user -> username == null || user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .filter(user -> email == null || user.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(user -> role == null || user.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    public String resetUserPassword(UUID id) {
        UserDocument user = getUserById(id);
        String newPassword = RandomStringUtils.randomAlphanumeric(12);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return newPassword;
    }

    public Map<String, Object> getUserActivity(UUID id, String period) {
        UserDocument user = getUserById(id);
        Map<String, Object> activity = new HashMap<>();
        
        activity.put("lastLogin", user.getLastLoginAt());
        activity.put("createdAt", user.getCreatedAt());
        activity.put("updatedAt", user.getUpdatedAt());
        activity.put("isActive", user.isEnabled()); 
        
        return activity;
    }

    public UserDocument findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void updateLastLogin(UUID id) {
        UserDocument user = getUserById(id);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserDocument activateUser(UUID id) {
        UserDocument user = getUserById(id);
        user.setEnabled(true);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserDocument deactivateUser(UUID id) {
        UserDocument user = getUserById(id);
        user.setEnabled(false);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
