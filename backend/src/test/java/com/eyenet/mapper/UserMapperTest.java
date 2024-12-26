package com.eyenet.mapper;

import com.eyenet.model.document.UserDocument;
import com.eyenet.model.entity.Role;
import com.eyenet.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private User testUser;
    private UserDocument testUserDoc;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        
        // Setup test user entity
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedpassword");
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName("ROLE_USER");
        roles.add(role);
        testUser.setRoles(roles);
        testUser.setActive(true);
        testUser.setLastLogin(testTime);
        testUser.setCreatedAt(testTime);
        testUser.setUpdatedAt(testTime);

        // Setup test user document
        testUserDoc = UserDocument.builder()
                .id(UUID.randomUUID().toString())
                .username("testuser")
                .email("test@example.com")
                .password("hashedpassword")
                .roles(Set.of("ROLE_USER"))
                .enabled(true)
                .lastLogin(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();
    }

    @Test
    void toDocument_shouldMapAllFields() {
        UserDocument result = userMapper.toDocument(testUser);
        
        assertNotNull(result);
        assertEquals(testUser.getId().toString(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getPassword(), result.getPassword());
        assertEquals(testUser.isActive(), result.isEnabled());
        assertEquals(testUser.getLastLogin(), result.getLastLogin());
        assertEquals(testUser.getCreatedAt(), result.getCreatedAt());
        assertEquals(testUser.getUpdatedAt(), result.getUpdatedAt());
        assertTrue(result.getRoles().contains("ROLE_USER"));
    }

    @Test
    void toEntity_shouldMapAllFields() {
        User result = userMapper.toEntity(testUserDoc);
        
        assertNotNull(result);
        assertEquals(testUserDoc.getUsername(), result.getUsername());
        assertEquals(testUserDoc.getEmail(), result.getEmail());
        assertEquals(testUserDoc.getPassword(), result.getPassword());
        assertEquals(testUserDoc.isEnabled(), result.isActive());
        assertEquals(testUserDoc.getLastLogin(), result.getLastLogin());
        assertEquals(testUserDoc.getCreatedAt(), result.getCreatedAt());
        assertEquals(testUserDoc.getUpdatedAt(), result.getUpdatedAt());
        assertTrue(result.getRoles().stream()
                .map(Role::getName)
                .anyMatch(name -> name.equals("ROLE_USER")));
    }

    @Test
    void toDocument_shouldHandleNullInput() {
        assertNull(userMapper.toDocument(null));
    }

    @Test
    void toEntity_shouldHandleNullInput() {
        assertNull(userMapper.toEntity(null));
    }
}
