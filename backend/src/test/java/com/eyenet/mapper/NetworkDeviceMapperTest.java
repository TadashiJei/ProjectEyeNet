package com.eyenet.mapper;

import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.dto.NetworkDeviceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NetworkDeviceMapperTest {

    private NetworkDeviceMapper deviceMapper;
    private NetworkDeviceDocument testDeviceDoc;
    private NetworkDeviceDTO testDeviceDto;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        deviceMapper = new NetworkDeviceMapper();
        
        // Setup test network device document
        testDeviceDoc = NetworkDeviceDocument.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Device")
                .ipAddress("192.168.1.1")
                .macAddress("00:11:22:33:44:55")
                .deviceType("SWITCH")
                .status("ACTIVE")
                .ports(Set.of(
                    NetworkDeviceDocument.PortInfo.builder()
                        .id(UUID.randomUUID().toString())
                        .number(1)
                        .status("UP")
                        .build()
                ))
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        // Setup test network device DTO
        testDeviceDto = NetworkDeviceDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Device")
                .ipAddress("192.168.1.1")
                .macAddress("00:11:22:33:44:55")
                .deviceType("SWITCH")
                .status("ACTIVE")
                .ports(Set.of(
                    NetworkDeviceDTO.PortInfo.builder()
                        .id(UUID.randomUUID().toString())
                        .number(1)
                        .status("UP")
                        .build()
                ))
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();
    }

    @Test
    void toDTO_shouldMapAllFields() {
        NetworkDeviceDTO result = deviceMapper.toDTO(testDeviceDoc);
        
        assertNotNull(result);
        assertEquals(testDeviceDoc.getId(), result.getId());
        assertEquals(testDeviceDoc.getName(), result.getName());
        assertEquals(testDeviceDoc.getIpAddress(), result.getIpAddress());
        assertEquals(testDeviceDoc.getMacAddress(), result.getMacAddress());
        assertEquals(testDeviceDoc.getDeviceType(), result.getDeviceType());
        assertEquals(testDeviceDoc.getStatus(), result.getStatus());
        assertEquals(testDeviceDoc.getCreatedAt(), result.getCreatedAt());
        assertEquals(testDeviceDoc.getUpdatedAt(), result.getUpdatedAt());
        
        // Test port mapping
        assertEquals(1, result.getPorts().size());
        NetworkDeviceDTO.PortInfo portInfo = result.getPorts().iterator().next();
        NetworkDeviceDocument.PortInfo originalPortInfo = testDeviceDoc.getPorts().iterator().next();
        assertEquals(originalPortInfo.getId(), portInfo.getId());
        assertEquals(originalPortInfo.getNumber(), portInfo.getNumber());
        assertEquals(originalPortInfo.getStatus(), portInfo.getStatus());
    }

    @Test
    void toDocument_shouldMapAllFields() {
        NetworkDeviceDocument result = deviceMapper.toDocument(testDeviceDto);
        
        assertNotNull(result);
        assertEquals(testDeviceDto.getId(), result.getId());
        assertEquals(testDeviceDto.getName(), result.getName());
        assertEquals(testDeviceDto.getIpAddress(), result.getIpAddress());
        assertEquals(testDeviceDto.getMacAddress(), result.getMacAddress());
        assertEquals(testDeviceDto.getDeviceType(), result.getDeviceType());
        assertEquals(testDeviceDto.getStatus(), result.getStatus());
        assertEquals(testDeviceDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(testDeviceDto.getUpdatedAt(), result.getUpdatedAt());
        
        // Test port mapping
        assertEquals(1, result.getPorts().size());
        NetworkDeviceDocument.PortInfo portInfo = result.getPorts().iterator().next();
        NetworkDeviceDTO.PortInfo originalPortInfo = testDeviceDto.getPorts().iterator().next();
        assertEquals(originalPortInfo.getId(), portInfo.getId());
        assertEquals(originalPortInfo.getNumber(), portInfo.getNumber());
        assertEquals(originalPortInfo.getStatus(), portInfo.getStatus());
    }

    @Test
    void toDTO_shouldHandleNullInput() {
        assertNull(deviceMapper.toDTO(null));
    }

    @Test
    void toDocument_shouldHandleNullInput() {
        assertNull(deviceMapper.toDocument(null));
    }
}
