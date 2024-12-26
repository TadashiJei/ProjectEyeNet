package com.eyenet.repository;

import com.eyenet.model.document.UserProfileDocument;
import com.eyenet.repository.mongodb.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    private UserProfileDocument testProfile;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        userProfileRepository.deleteAll();

        testProfile = UserProfileDocument.builder()
                .id(UUID.randomUUID().toString())
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .department("IT")
                .role("ADMIN")
                .status("ACTIVE")
                .lastLogin(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        userProfileRepository.save(testProfile);
    }

    @Test
    void findById_shouldReturnProfile() {
        Optional<UserProfileDocument> found = userProfileRepository.findById(testProfile.getId());
        assertTrue(found.isPresent());
        assertEquals(testProfile.getUsername(), found.get().getUsername());
        assertEquals(testProfile.getEmail(), found.get().getEmail());
        assertEquals(testProfile.getFirstName(), found.get().getFirstName());
        assertEquals(testProfile.getLastName(), found.get().getLastName());
        assertEquals(testProfile.getDepartment(), found.get().getDepartment());
        assertEquals(testProfile.getRole(), found.get().getRole());
        assertEquals(testProfile.getStatus(), found.get().getStatus());
        assertEquals(testProfile.getLastLogin(), found.get().getLastLogin());
        assertEquals(testProfile.getCreatedAt(), found.get().getCreatedAt());
        assertEquals(testProfile.getUpdatedAt(), found.get().getUpdatedAt());
    }

    @Test
    void findByUsername_shouldReturnProfile() {
        Optional<UserProfileDocument> found = userProfileRepository.findByUsername(testProfile.getUsername());
        assertTrue(found.isPresent());
        assertEquals(testProfile.getId(), found.get().getId());
    }

    @Test
    void findByEmail_shouldReturnProfile() {
        Optional<UserProfileDocument> found = userProfileRepository.findByEmail(testProfile.getEmail());
        assertTrue(found.isPresent());
        assertEquals(testProfile.getId(), found.get().getId());
    }

    @Test
    void findByDepartment_shouldReturnProfiles() {
        List<UserProfileDocument> found = userProfileRepository.findByDepartment(testProfile.getDepartment());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testProfile.getId(), found.get(0).getId());
    }

    @Test
    void findByRole_shouldReturnProfiles() {
        List<UserProfileDocument> found = userProfileRepository.findByRole(testProfile.getRole());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testProfile.getId(), found.get(0).getId());
    }

    @Test
    void findByStatus_shouldReturnProfiles() {
        List<UserProfileDocument> found = userProfileRepository.findByStatus(testProfile.getStatus());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testProfile.getId(), found.get(0).getId());
    }

    @Test
    void findByLastLoginBetween_shouldReturnProfiles() {
        LocalDateTime start = testTime.minusHours(1);
        LocalDateTime end = testTime.plusHours(1);
        List<UserProfileDocument> found = userProfileRepository.findByLastLoginBetween(start, end);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testProfile.getId(), found.get(0).getId());
    }

    @Test
    void findAll_withPagination_shouldReturnPagedProfiles() {
        Page<UserProfileDocument> page = userProfileRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "lastLogin")));
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(testProfile.getId(), page.getContent().get(0).getId());
    }

    @Test
    void save_shouldCreateNewProfile() {
        UserProfileDocument newProfile = UserProfileDocument.builder()
                .id(UUID.randomUUID().toString())
                .username("newuser")
                .email("new@example.com")
                .firstName("New")
                .lastName("User")
                .department("HR")
                .role("USER")
                .status("INACTIVE")
                .lastLogin(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        UserProfileDocument saved = userProfileRepository.save(newProfile);
        assertNotNull(saved.getId());
        assertEquals(newProfile.getUsername(), saved.getUsername());
        assertEquals(newProfile.getEmail(), saved.getEmail());
        assertEquals(newProfile.getFirstName(), saved.getFirstName());
        assertEquals(newProfile.getLastName(), saved.getLastName());
        assertEquals(newProfile.getDepartment(), saved.getDepartment());
        assertEquals(newProfile.getRole(), saved.getRole());
        assertEquals(newProfile.getStatus(), saved.getStatus());
        assertEquals(newProfile.getLastLogin(), saved.getLastLogin());
        assertEquals(newProfile.getCreatedAt(), saved.getCreatedAt());
        assertEquals(newProfile.getUpdatedAt(), saved.getUpdatedAt());
    }

    @Test
    void delete_shouldRemoveProfile() {
        userProfileRepository.delete(testProfile);
        Optional<UserProfileDocument> found = userProfileRepository.findById(testProfile.getId());
        assertFalse(found.isPresent());
    }
}
