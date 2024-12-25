package com.eyenet;

import com.eyenet.model.Role;
import com.eyenet.model.document.UserDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BasicIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
        // This test verifies that Spring context loads successfully
    }

    @Test
    void testMongoConnection() {
        // Create a test user
        UserDocument user = new UserDocument();
        user.setUsername("testuser");
        user.setPasswordHash("testhash");
        user.setRole(Role.ADMIN);

        // Save to MongoDB
        UserDocument savedUser = mongoTemplate.save(user);

        // Verify
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());

        // Cleanup
        mongoTemplate.remove(savedUser);
    }
}
