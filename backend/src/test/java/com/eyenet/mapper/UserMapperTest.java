package com.eyenet.mapper;

import com.eyenet.model.document.RoleDocument;
import com.eyenet.model.document.UserDocument;
import com.eyenet.model.dto.UserProfileDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private UserDocument testUserDoc;
    private UserProfileDTO testUserDto;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        
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

        // Setup test user DTO
        testUserDto = UserProfileDTO.builder()
                .id(UUID.randomUUID().toString())
                .username("testuser")
                .email("test@example.com")
                .roles(Set.of("ROLE_USER"))
                .enabled(true)
                .lastLogin(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        UserProfileDTO result = userMapper.toDTO(testUserDoc);
        
        assertNotNull(result);
        assertEquals(testUserDoc.getId(), result.getId());
        assertEquals(testUserDoc.getUsername(), result.getUsername());
        assertEquals(testUserDoc.getEmail(), result.getEmail());
        assertEquals(testUserDoc.isEnabled(), result.isEnabled());
        assertEquals(testUserDoc.getLastLogin(), result.getLastLogin());
        assertEquals(testUserDoc.getCreatedAt(), result.getCreatedAt());
        assertEquals(testUserDoc.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(testUserDoc.getRoles(), result.getRoles());
    }

    @Test
    void toDocument_shouldMapAllFields() {
        UserDocument result = userMapper.toDocument(testUserDto);
        
        assertNotNull(result);
        assertEquals(testUserDto.getUsername(), result.getUsername());
        assertEquals(testUserDto.getEmail(), result.getEmail());
        assertEquals(testUserDto.isEnabled(), result.isEnabled());
        assertEquals(testUserDto.getLastLogin(), result.getLastLogin());
        assertEquals(testUserDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(testUserDto.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(testUserDto.getRoles(), result.getRoles());
    }

    @Test
    void toDTO_shouldHandleNullInput() {
        assertNull(userMapper.toDTO(null));
    }

    @Test
    void toDocument_shouldHandleNullInput() {
        assertNull(userMapper.toDocument(null));
    }
}
