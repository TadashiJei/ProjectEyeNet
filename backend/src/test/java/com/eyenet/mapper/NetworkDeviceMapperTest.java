package com.eyenet.mapper;

import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.dto.NetworkDeviceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NetworkDeviceMapperTest {

    private NetworkDeviceMapper deviceMapper;
    private NetworkDeviceDocument testDeviceDoc;
    private NetworkDeviceDTO testDeviceDto;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deviceMapper = new NetworkDeviceMapper(objectMapper);
        
        UUID deviceId = UUID.randomUUID();
        UUID portId = UUID.randomUUID();

        // Setup test network device document
        testDeviceDoc = NetworkDeviceDocument.builder()
                .id(deviceId)
                .name("Test Device")
                .ipAddress("192.168.1.1")
                .macAddress("00:11:22:33:44:55")
                .deviceType(NetworkDeviceDocument.DeviceType.SWITCH)
                .status("ACTIVE")
                .ports(Set.of(
                    NetworkDeviceDocument.PortInfo.builder()
                        .id(portId)
                        .number(1)
                        .status("UP")
                        .build()
                ))
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        // Setup test network device DTO
        testDeviceDto = NetworkDeviceDTO.builder()
                .id(deviceId)
                .name("Test Device")
                .ipAddress("192.168.1.1")
                .macAddress("00:11:22:33:44:55")
                .deviceType("SWITCH")
                .status("ACTIVE")
                .ports(Set.of(
                    NetworkDeviceDTO.PortInfo.builder()
                        .id(portId)
                        .number(1)
                        .status("UP")
                        .build()
                ))
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();
    }

    @Test
    void toDTO_ShouldMapDocumentToDTO() {
        NetworkDeviceDTO result = deviceMapper.toDTO(testDeviceDoc);
        
        assertNotNull(result);
        assertEquals(testDeviceDoc.getId(), result.getId());
        assertEquals(testDeviceDoc.getName(), result.getName());
        assertEquals(testDeviceDoc.getIpAddress(), result.getIpAddress());
        assertEquals(testDeviceDoc.getMacAddress(), result.getMacAddress());
        assertEquals(testDeviceDoc.getDeviceType().toString(), result.getDeviceType());
        assertEquals(testDeviceDoc.getStatus(), result.getStatus());
        
        // Test port mapping
        assertNotNull(result.getPorts());
        assertEquals(1, result.getPorts().size());
        
        NetworkDeviceDocument.PortInfo originalPortInfo = testDeviceDoc.getPorts().iterator().next();
        NetworkDeviceDTO.PortInfo mappedPortInfo = result.getPorts().iterator().next();
        
        assertEquals(originalPortInfo.getId(), mappedPortInfo.getId());
        assertEquals(originalPortInfo.getNumber(), mappedPortInfo.getNumber());
        assertEquals(originalPortInfo.getStatus(), mappedPortInfo.getStatus());
    }

    @Test
    void toDocument_ShouldMapDTOToDocument() {
        NetworkDeviceDocument result = deviceMapper.toDocument(testDeviceDto);
        
        assertNotNull(result);
        assertEquals(testDeviceDto.getId(), result.getId());
        assertEquals(testDeviceDto.getName(), result.getName());
        assertEquals(testDeviceDto.getIpAddress(), result.getIpAddress());
        assertEquals(testDeviceDto.getMacAddress(), result.getMacAddress());
        assertEquals(testDeviceDto.getDeviceType(), result.getDeviceType().toString());
        assertEquals(testDeviceDto.getStatus(), result.getStatus());
        
        // Test port mapping
        assertNotNull(result.getPorts());
        assertEquals(1, result.getPorts().size());
        
        NetworkDeviceDTO.PortInfo portInfo = testDeviceDto.getPorts().iterator().next();
        NetworkDeviceDocument.PortInfo mappedPortInfo = result.getPorts().iterator().next();
        
        assertEquals(portInfo.getId(), mappedPortInfo.getId());
        assertEquals(portInfo.getNumber(), mappedPortInfo.getNumber());
        assertEquals(portInfo.getStatus(), mappedPortInfo.getStatus());
    }

    @Test
    void toDTO_ShouldReturnNullForNullInput() {
        assertNull(deviceMapper.toDTO(null));
    }

    @Test
    void toDocument_ShouldReturnNullForNullInput() {
        assertNull(deviceMapper.toDocument(null));
    }
}
