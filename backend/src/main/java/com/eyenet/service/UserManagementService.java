package com.eyenet.service;

import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDocument> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDocument getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDocument createUser(UserDocument user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserDocument updateUser(String id, UserDocument user) {
        UserDocument existingUser = getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existingUser.setRoles(user.getRoles());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existingUser);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserDocument activateUser(String id) {
        UserDocument user = getUserById(id);
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserDocument deactivateUser(String id) {
        UserDocument user = getUserById(id);
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public List<UserDocument> searchUsers(String username, String email, String role) {
        List<UserDocument> users = userRepository.findAll();
        
        return users.stream()
                .filter(user -> username == null || user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .filter(user -> email == null || user.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(user -> role == null || user.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    public String resetUserPassword(String id) {
        UserDocument user = getUserById(id);
        String newPassword = RandomStringUtils.randomAlphanumeric(12);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return newPassword;
    }

    public Map<String, Object> getUserActivity(String id, String period) {
        UserDocument user = getUserById(id);
        Map<String, Object> activity = new HashMap<>();
        
        activity.put("lastLogin", user.getLastLogin());
        activity.put("createdAt", user.getCreatedAt());
        activity.put("updatedAt", user.getUpdatedAt());
        activity.put("isActive", user.isActive()); // Changed from isEnabled() to isActive()
        
        return activity;
    }

    public UserDocument findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
