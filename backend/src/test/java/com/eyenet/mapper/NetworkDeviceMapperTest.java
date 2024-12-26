package com.eyenet.mapper;

import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.model.entity.Port;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NetworkDeviceMapperTest {

    private NetworkDeviceMapper deviceMapper;
    private NetworkDevice testDevice;
    private NetworkDeviceDocument testDeviceDoc;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        deviceMapper = new NetworkDeviceMapper();
        
        // Setup test network device entity
        testDevice = new NetworkDevice();
        testDevice.setId(UUID.randomUUID());
        testDevice.setName("Test Device");
        testDevice.setIpAddress("192.168.1.1");
        testDevice.setMacAddress("00:11:22:33:44:55");
        testDevice.setDeviceType("SWITCH");
        testDevice.setStatus("ACTIVE");
        Set<Port> ports = new HashSet<>();
        Port port = new Port();
        port.setId(UUID.randomUUID());
        port.setNumber(1);
        port.setStatus("UP");
        ports.add(port);
        testDevice.setPorts(ports);
        testDevice.setCreatedAt(testTime);
        testDevice.setUpdatedAt(testTime);

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
    }

    @Test
    void toDocument_shouldMapAllFields() {
        NetworkDeviceDocument result = deviceMapper.toDocument(testDevice);
        
        assertNotNull(result);
        assertEquals(testDevice.getId().toString(), result.getId());
        assertEquals(testDevice.getName(), result.getName());
        assertEquals(testDevice.getIpAddress(), result.getIpAddress());
        assertEquals(testDevice.getMacAddress(), result.getMacAddress());
        assertEquals(testDevice.getDeviceType(), result.getDeviceType());
        assertEquals(testDevice.getStatus(), result.getStatus());
        assertEquals(testDevice.getCreatedAt(), result.getCreatedAt());
        assertEquals(testDevice.getUpdatedAt(), result.getUpdatedAt());
        
        // Test port mapping
        assertEquals(1, result.getPorts().size());
        NetworkDeviceDocument.PortInfo portInfo = result.getPorts().iterator().next();
        Port originalPort = testDevice.getPorts().iterator().next();
        assertEquals(originalPort.getId().toString(), portInfo.getId());
        assertEquals(originalPort.getNumber(), portInfo.getNumber());
        assertEquals(originalPort.getStatus(), portInfo.getStatus());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        NetworkDevice result = deviceMapper.toEntity(testDeviceDoc);
        
        assertNotNull(result);
        assertEquals(UUID.fromString(testDeviceDoc.getId()), result.getId());
        assertEquals(testDeviceDoc.getName(), result.getName());
        assertEquals(testDeviceDoc.getIpAddress(), result.getIpAddress());
        assertEquals(testDeviceDoc.getMacAddress(), result.getMacAddress());
        assertEquals(testDeviceDoc.getDeviceType(), result.getDeviceType());
        assertEquals(testDeviceDoc.getStatus(), result.getStatus());
        assertEquals(testDeviceDoc.getCreatedAt(), result.getCreatedAt());
        assertEquals(testDeviceDoc.getUpdatedAt(), result.getUpdatedAt());
        
        // Test port mapping
        assertEquals(1, result.getPorts().size());
        Port port = result.getPorts().iterator().next();
        NetworkDeviceDocument.PortInfo originalPortInfo = testDeviceDoc.getPorts().iterator().next();
        assertEquals(UUID.fromString(originalPortInfo.getId()), port.getId());
        assertEquals(originalPortInfo.getNumber(), port.getNumber());
        assertEquals(originalPortInfo.getStatus(), port.getStatus());
    }

    @Test
    void toDocument_shouldHandleNullInput() {
        assertNull(deviceMapper.toDocument(null));
    }

    @Test
    void toEntity_shouldHandleNullInput() {
        assertNull(deviceMapper.toEntity(null));
    }
}
