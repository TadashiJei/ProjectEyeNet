package com.eyenet.repository;

import com.eyenet.model.document.NetworkDeviceDocument;
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
class NetworkDeviceRepositoryTest {

    @Autowired
    private NetworkDeviceRepository networkDeviceRepository;

    private NetworkDeviceDocument testDevice;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setUp() {
        networkDeviceRepository.deleteAll();

        testDevice = NetworkDeviceDocument.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Device")
                .description("Test Description")
                .ipAddress("192.168.1.1")
                .macAddress("00:11:22:33:44:55")
                .manufacturer("Test Manufacturer")
                .model("Test Model")
                .firmwareVersion("1.0.0")
                .status("ACTIVE")
                .lastSeen(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        networkDeviceRepository.save(testDevice);
    }

    @Test
    void findById_shouldReturnDevice() {
        Optional<NetworkDeviceDocument> found = networkDeviceRepository.findById(testDevice.getId());
        assertTrue(found.isPresent());
        assertEquals(testDevice.getName(), found.get().getName());
        assertEquals(testDevice.getDescription(), found.get().getDescription());
        assertEquals(testDevice.getIpAddress(), found.get().getIpAddress());
        assertEquals(testDevice.getMacAddress(), found.get().getMacAddress());
        assertEquals(testDevice.getManufacturer(), found.get().getManufacturer());
        assertEquals(testDevice.getModel(), found.get().getModel());
        assertEquals(testDevice.getFirmwareVersion(), found.get().getFirmwareVersion());
        assertEquals(testDevice.getStatus(), found.get().getStatus());
        assertEquals(testDevice.getLastSeen(), found.get().getLastSeen());
        assertEquals(testDevice.getCreatedAt(), found.get().getCreatedAt());
        assertEquals(testDevice.getUpdatedAt(), found.get().getUpdatedAt());
    }

    @Test
    void findByIpAddress_shouldReturnDevice() {
        Optional<NetworkDeviceDocument> found = networkDeviceRepository.findByIpAddress(testDevice.getIpAddress());
        assertTrue(found.isPresent());
        assertEquals(testDevice.getId(), found.get().getId());
    }

    @Test
    void findByMacAddress_shouldReturnDevice() {
        Optional<NetworkDeviceDocument> found = networkDeviceRepository.findByMacAddress(testDevice.getMacAddress());
        assertTrue(found.isPresent());
        assertEquals(testDevice.getId(), found.get().getId());
    }

    @Test
    void findByStatus_shouldReturnDevices() {
        List<NetworkDeviceDocument> found = networkDeviceRepository.findByStatus(testDevice.getStatus());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testDevice.getId(), found.get(0).getId());
    }

    @Test
    void findByManufacturer_shouldReturnDevices() {
        List<NetworkDeviceDocument> found = networkDeviceRepository.findByManufacturer(testDevice.getManufacturer());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testDevice.getId(), found.get(0).getId());
    }

    @Test
    void findByModel_shouldReturnDevices() {
        List<NetworkDeviceDocument> found = networkDeviceRepository.findByModel(testDevice.getModel());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testDevice.getId(), found.get(0).getId());
    }

    @Test
    void findByFirmwareVersion_shouldReturnDevices() {
        List<NetworkDeviceDocument> found = networkDeviceRepository.findByFirmwareVersion(testDevice.getFirmwareVersion());
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testDevice.getId(), found.get(0).getId());
    }

    @Test
    void findByLastSeenBetween_shouldReturnDevices() {
        LocalDateTime start = testTime.minusHours(1);
        LocalDateTime end = testTime.plusHours(1);
        List<NetworkDeviceDocument> found = networkDeviceRepository.findByLastSeenBetween(start, end);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testDevice.getId(), found.get(0).getId());
    }

    @Test
    void findAll_withPagination_shouldReturnPagedDevices() {
        Page<NetworkDeviceDocument> page = networkDeviceRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "lastSeen")));
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(testDevice.getId(), page.getContent().get(0).getId());
    }

    @Test
    void save_shouldCreateNewDevice() {
        NetworkDeviceDocument newDevice = NetworkDeviceDocument.builder()
                .id(UUID.randomUUID().toString())
                .name("New Device")
                .description("New Description")
                .ipAddress("192.168.1.2")
                .macAddress("00:11:22:33:44:66")
                .manufacturer("New Manufacturer")
                .model("New Model")
                .firmwareVersion("1.0.1")
                .status("INACTIVE")
                .lastSeen(testTime)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();

        NetworkDeviceDocument saved = networkDeviceRepository.save(newDevice);
        assertNotNull(saved.getId());
        assertEquals(newDevice.getName(), saved.getName());
        assertEquals(newDevice.getDescription(), saved.getDescription());
        assertEquals(newDevice.getIpAddress(), saved.getIpAddress());
        assertEquals(newDevice.getMacAddress(), saved.getMacAddress());
        assertEquals(newDevice.getManufacturer(), saved.getManufacturer());
        assertEquals(newDevice.getModel(), saved.getModel());
        assertEquals(newDevice.getFirmwareVersion(), saved.getFirmwareVersion());
        assertEquals(newDevice.getStatus(), saved.getStatus());
        assertEquals(newDevice.getLastSeen(), saved.getLastSeen());
        assertEquals(newDevice.getCreatedAt(), saved.getCreatedAt());
        assertEquals(newDevice.getUpdatedAt(), saved.getUpdatedAt());
    }

    @Test
    void delete_shouldRemoveDevice() {
        networkDeviceRepository.delete(testDevice);
        Optional<NetworkDeviceDocument> found = networkDeviceRepository.findById(testDevice.getId());
        assertFalse(found.isPresent());
    }
}
