package com.eyenet.mapper;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.entity.FlowRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlowRuleMapperTest {

    private FlowRuleMapper flowRuleMapper;
    private FlowRule testRule;
    private FlowRuleDocument testRuleDoc;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        flowRuleMapper = new FlowRuleMapper();
        
        // Setup test flow rule entity
        testRule = new FlowRule();
        testRule.setId(UUID.randomUUID());
        testRule.setName("Test Rule");
        testRule.setDescription("Test Description");
        testRule.setPriority(1);
        testRule.setEnabled(true);
        testRule.setCreatedAt(testTime);
        testRule.setUpdatedAt(testTime);

        // Setup test flow rule document
        testRuleDoc = FlowRuleDocument.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Rule")
                .description("Test Description")
                .priority(1)
                .enabled(true)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();
    }

    @Test
    void toDocument_shouldMapAllFields() {
        FlowRuleDocument result = flowRuleMapper.toDocument(testRule);
        
        assertNotNull(result);
        assertEquals(testRule.getId().toString(), result.getId());
        assertEquals(testRule.getName(), result.getName());
        assertEquals(testRule.getDescription(), result.getDescription());
        assertEquals(testRule.getPriority(), result.getPriority());
        assertEquals(testRule.isEnabled(), result.isEnabled());
        assertEquals(testRule.getCreatedAt(), result.getCreatedAt());
        assertEquals(testRule.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        FlowRule result = flowRuleMapper.toEntity(testRuleDoc);
        
        assertNotNull(result);
        assertEquals(UUID.fromString(testRuleDoc.getId()), result.getId());
        assertEquals(testRuleDoc.getName(), result.getName());
        assertEquals(testRuleDoc.getDescription(), result.getDescription());
        assertEquals(testRuleDoc.getPriority(), result.getPriority());
        assertEquals(testRuleDoc.isEnabled(), result.isEnabled());
        assertEquals(testRuleDoc.getCreatedAt(), result.getCreatedAt());
        assertEquals(testRuleDoc.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toDocument_shouldHandleNullInput() {
        assertNull(flowRuleMapper.toDocument(null));
    }

    @Test
    void toEntity_shouldHandleNullInput() {
        assertNull(flowRuleMapper.toEntity(null));
    }
}
