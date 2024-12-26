package com.eyenet.repository;

import com.eyenet.model.document.FlowRuleDocument;
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
class FlowRuleRepositoryTest {

    @Autowired
    private FlowRuleRepository flowRuleRepository;

    private FlowRuleDocument testRule;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        flowRuleRepository.deleteAll();

        testRule = FlowRuleDocument.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(UUID.randomUUID().toString())
                .name("Test Rule")
                .description("Test Description")
                .tableId(0)
                .priority(100)
                .timeoutIdle(30)
                .timeoutHard(300)
                .cookie(123L)
                .enabled(true)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        flowRuleRepository.save(testRule);
    }

    @Test
    void findById_shouldReturnRule() {
        Optional<FlowRuleDocument> found = flowRuleRepository.findById(testRule.getId());
        assertTrue(found.isPresent());
        assertEquals(testRule.getName(), found.get().getName());
        assertEquals(testRule.getDescription(), found.get().getDescription());
        assertEquals(testRule.getDeviceId(), found.get().getDeviceId());
        assertEquals(testRule.getTableId(), found.get().getTableId());
        assertEquals(testRule.getPriority(), found.get().getPriority());
        assertEquals(testRule.getTimeoutIdle(), found.get().getTimeoutIdle());
        assertEquals(testRule.getTimeoutHard(), found.get().getTimeoutHard());
        assertEquals(testRule.getCookie(), found.get().getCookie());
        assertEquals(testRule.isEnabled(), found.get().isEnabled());
        assertEquals(testRule.getCreatedAt(), found.get().getCreatedAt());
        assertEquals(testRule.getUpdatedAt(), found.get().getUpdatedAt());
    }

    @Test
    void findByDeviceId_shouldReturnRules() {
        List<FlowRuleDocument> found = flowRuleRepository.findByDeviceId(testRule.getDeviceId());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testRule.getId(), found.get(0).getId());
    }

    @Test
    void findByTableId_shouldReturnRules() {
        List<FlowRuleDocument> found = flowRuleRepository.findByTableId(testRule.getTableId());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testRule.getId(), found.get(0).getId());
    }

    @Test
    void findByPriority_shouldReturnRules() {
        List<FlowRuleDocument> found = flowRuleRepository.findByPriority(testRule.getPriority());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testRule.getId(), found.get(0).getId());
    }

    @Test
    void findByEnabled_shouldReturnRules() {
        List<FlowRuleDocument> found = flowRuleRepository.findByEnabled(testRule.isEnabled());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testRule.getId(), found.get(0).getId());
    }

    @Test
    void findByDeviceIdAndTableId_shouldReturnRules() {
        List<FlowRuleDocument> found = flowRuleRepository.findByDeviceIdAndTableId(
                testRule.getDeviceId(), testRule.getTableId());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testRule.getId(), found.get(0).getId());
    }

    @Test
    void findByDeviceIdAndEnabled_shouldReturnRules() {
        List<FlowRuleDocument> found = flowRuleRepository.findByDeviceIdAndEnabled(
                testRule.getDeviceId(), testRule.isEnabled());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testRule.getId(), found.get(0).getId());
    }

    @Test
    void findAll_withPagination_shouldReturnPagedRules() {
        Page<FlowRuleDocument> page = flowRuleRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "priority")));
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(testRule.getId(), page.getContent().get(0).getId());
    }

    @Test
    void save_shouldCreateNewRule() {
        FlowRuleDocument newRule = FlowRuleDocument.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(UUID.randomUUID().toString())
                .name("New Rule")
                .description("New Description")
                .tableId(1)
                .priority(200)
                .timeoutIdle(60)
                .timeoutHard(600)
                .cookie(456L)
                .enabled(false)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        FlowRuleDocument saved = flowRuleRepository.save(newRule);
        assertNotNull(saved.getId());
        assertEquals(newRule.getName(), saved.getName());
        assertEquals(newRule.getDescription(), saved.getDescription());
        assertEquals(newRule.getDeviceId(), saved.getDeviceId());
        assertEquals(newRule.getTableId(), saved.getTableId());
        assertEquals(newRule.getPriority(), saved.getPriority());
        assertEquals(newRule.getTimeoutIdle(), saved.getTimeoutIdle());
        assertEquals(newRule.getTimeoutHard(), saved.getTimeoutHard());
        assertEquals(newRule.getCookie(), saved.getCookie());
        assertEquals(newRule.isEnabled(), saved.isEnabled());
        assertEquals(newRule.getCreatedAt(), saved.getCreatedAt());
        assertEquals(newRule.getUpdatedAt(), saved.getUpdatedAt());
    }

    @Test
    void delete_shouldRemoveRule() {
        flowRuleRepository.delete(testRule);
        Optional<FlowRuleDocument> found = flowRuleRepository.findById(testRule.getId());
        assertFalse(found.isPresent());
    }
}
