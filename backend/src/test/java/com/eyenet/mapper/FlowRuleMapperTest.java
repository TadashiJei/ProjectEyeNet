package com.eyenet.mapper;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.dto.FlowRuleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlowRuleMapperTest {

    private FlowRuleMapper flowRuleMapper;
    private FlowRuleDocument testRuleDoc;
    private FlowRuleDTO testRuleDto;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        flowRuleMapper = new FlowRuleMapper();
        
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

        // Setup test flow rule DTO
        testRuleDto = FlowRuleDTO.builder()
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
    void toDTO_shouldMapAllFields() {
        FlowRuleDTO result = flowRuleMapper.toDTO(testRuleDoc);
        
        assertNotNull(result);
        assertEquals(testRuleDoc.getId(), result.getId());
        assertEquals(testRuleDoc.getName(), result.getName());
        assertEquals(testRuleDoc.getDescription(), result.getDescription());
        assertEquals(testRuleDoc.getPriority(), result.getPriority());
        assertEquals(testRuleDoc.isEnabled(), result.isEnabled());
        assertEquals(testRuleDoc.getCreatedAt(), result.getCreatedAt());
        assertEquals(testRuleDoc.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toDocument_shouldMapAllFields() {
        FlowRuleDocument result = flowRuleMapper.toDocument(testRuleDto);
        
        assertNotNull(result);
        assertEquals(testRuleDto.getId(), result.getId());
        assertEquals(testRuleDto.getName(), result.getName());
        assertEquals(testRuleDto.getDescription(), result.getDescription());
        assertEquals(testRuleDto.getPriority(), result.getPriority());
        assertEquals(testRuleDto.isEnabled(), result.isEnabled());
        assertEquals(testRuleDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(testRuleDto.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toDTO_shouldHandleNullInput() {
        assertNull(flowRuleMapper.toDTO(null));
    }

    @Test
    void toDocument_shouldHandleNullInput() {
        assertNull(flowRuleMapper.toDocument(null));
    }
}
