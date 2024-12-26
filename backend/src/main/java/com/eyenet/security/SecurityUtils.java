package com.eyenet.security;

import com.eyenet.model.document.UserDocument;
import com.eyenet.model.entity.User;
import com.eyenet.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserMapper userMapper;

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return UUID.fromString(((User) authentication.getPrincipal()).getId());
        }
        throw new SecurityException("No authenticated user found");
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new SecurityException("No authenticated user found");
    }

    public UserDocument getCurrentUserDocument() {
        User user = getCurrentUser();
        return userMapper.mapToUserDocument(user);
    }

    public String getCurrentSessionToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getCredentials().toString();
        }
        throw new SecurityException("No session token found");
    }

    public boolean isCurrentUser(UUID userId) {
        return getCurrentUserId().equals(userId);
    }
}
