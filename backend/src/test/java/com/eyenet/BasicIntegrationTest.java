package com.eyenet;

import com.eyenet.config.TestConfig;
import com.eyenet.model.document.UserDocument;
import com.eyenet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class BasicIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // Verify Spring context loads successfully
    }

    @Test
    void testMongoConnection() {
        UserDocument user = new UserDocument();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");

        UserDocument savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());

        userRepository.deleteById(savedUser.getId());
    }
}
