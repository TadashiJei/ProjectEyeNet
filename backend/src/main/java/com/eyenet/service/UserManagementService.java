package com.eyenet.service;

import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.mongodb.UserRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public UserDocument createUser(UserDocument user) {
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public UserDocument updateUser(UUID id, UserDocument updatedUser) {
        UserDocument existingUser = getUserById(id);

        updatedUser.setId(id);
        updatedUser.setCreatedAt(existingUser.getCreatedAt());
        updatedUser.setUpdatedAt(LocalDateTime.now());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().equals(existingUser.getPassword())) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        updatedUser.setFirstName(updatedUser.getFirstName());
        updatedUser.setLastName(updatedUser.getLastName());
        updatedUser.setEmail(updatedUser.getEmail());
        updatedUser.setPhoneNumber(updatedUser.getPhoneNumber());
        updatedUser.setJobTitle(updatedUser.getJobTitle());
        updatedUser.setDepartmentId(updatedUser.getDepartmentId());
        updatedUser.setDepartmentName(updatedUser.getDepartmentName());
        updatedUser.setProfilePicture(updatedUser.getProfilePicture());
        updatedUser.setEnabled(updatedUser.isEnabled());
        updatedUser.setTimezone(updatedUser.getTimezone());
        updatedUser.setLanguage(updatedUser.getLanguage());
        updatedUser.setTheme(updatedUser.getTheme());

        return userRepository.save(updatedUser);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
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

    public Map<String, Object> getUserActivity(UUID id) {
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
