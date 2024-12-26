package com.eyenet.service;

import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.repository.NetworkDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NetworkDeviceService {
    private final NetworkDeviceRepository networkDeviceRepository;

    public List<NetworkDevice> getAllDevices() {
        return networkDeviceRepository.findAll();
    }

    public NetworkDevice getDeviceById(UUID id) {
        return networkDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Network device not found"));
    }

    public NetworkDevice getDevice(UUID id) {
        return getDeviceById(id);
    }

    public NetworkDevice createDevice(NetworkDevice device) {
        return networkDeviceRepository.save(device);
    }

    public NetworkDevice updateDevice(UUID id, NetworkDevice device) {
        NetworkDevice existingDevice = getDeviceById(id);
        existingDevice.setName(device.getName());
        existingDevice.setIpAddress(device.getIpAddress());
        existingDevice.setMacAddress(device.getMacAddress());
        existingDevice.setType(device.getType());
        existingDevice.setStatus(device.getStatus());
        return networkDeviceRepository.save(existingDevice);
    }

    public void deleteDevice(UUID id) {
        networkDeviceRepository.deleteById(id);
    }
}
