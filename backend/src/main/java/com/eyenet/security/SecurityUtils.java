package com.eyenet.security;

import com.eyenet.model.document.UserDocument;
import com.eyenet.model.entity.User;
import com.eyenet.repository.UserRepository;
import com.eyenet.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public boolean hasPermission(Authentication authentication, String permission) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(permission));
    }

    public boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(role) || a.equals("ROLE_" + role));
    }

    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        UserDocument userDoc = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return userMapper.mapToUser(userDoc);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN");
    }

    public boolean isSuperAdmin(Authentication authentication) {
        return hasRole(authentication, "SUPER_ADMIN");
    }

    public boolean isDepartmentManager(Authentication authentication) {
        return hasRole(authentication, "DEPARTMENT_MANAGER");
    }

    public boolean canModifyUser(Authentication authentication, User targetUser) {
        User currentUser = getCurrentUser(authentication);
        
        // Admins can modify any user
        if (isAdmin(authentication)) {
            return true;
        }
        
        // Department managers can modify users in their department
        if (isDepartmentManager(authentication) && 
            currentUser.getDepartment() != null && 
            currentUser.getDepartment().equals(targetUser.getDepartment())) {
            return true;
        }
        
        // Users can modify themselves
        return currentUser.getId().equals(targetUser.getId());
    }

    public boolean canAccessResource(Authentication authentication, String resourceType, String action) {
        String requiredPermission = resourceType + "_" + action;
        return hasPermission(authentication, requiredPermission) || isAdmin(authentication);
    }

    public User getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found");
        }
        return getCurrentUser(authentication);
    }

    public String getCurrentSession() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found");
        }
        return authentication.getName() + "_" + System.currentTimeMillis();
    }
}