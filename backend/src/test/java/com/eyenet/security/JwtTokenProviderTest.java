package com.eyenet.security;

import com.eyenet.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        jwtConfig = new JwtConfig();
        ReflectionTestUtils.setField(jwtConfig, "jwtSecret", "dGhpc2lzYXRlc3RzZWNyZXRrZXlmb3JleWVuZXRzZGNvbnRyb2xsZXJ0ZXN0aW5n");
        ReflectionTestUtils.setField(jwtConfig, "jwtExpiration", 3600000L);
        tokenProvider = new JwtTokenProvider(jwtConfig);
    }

    @Test
    void generateTokenAndValidate() {
        // Create test user
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        // Generate token
        String token = tokenProvider.generateToken(authentication);
        assertNotNull(token);

        // Validate token
        assertTrue(tokenProvider.validateToken(token));

        // Verify username
        assertEquals("testuser", tokenProvider.getUsernameFromToken(token));
    }

    @Test
    void validateInvalidToken() {
        assertFalse(tokenProvider.validateToken("invalid.token.here"));
    }
}
