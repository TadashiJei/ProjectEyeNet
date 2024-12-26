package com.eyenet.service;

import com.eyenet.model.document.NetworkConfigDocument;
import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.document.PortDocument;
import com.eyenet.model.document.QoSPolicyDocument;
import com.eyenet.repository.mongodb.NetworkConfigurationRepository;
import com.eyenet.repository.mongodb.NetworkDeviceRepository;
import com.eyenet.repository.mongodb.PortRepository;
import com.eyenet.repository.mongodb.QoSPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NetworkConfigurationService {
    private final NetworkConfigurationRepository networkConfigurationRepository;
    private final NetworkDeviceRepository networkDeviceRepository;
    private final PortRepository portRepository;
    private final QoSPolicyRepository qosPolicyRepository;

    @Transactional
    public NetworkDeviceDocument registerDevice(NetworkDeviceDocument device) {
        if (networkDeviceRepository.existsByDeviceId(device.getDeviceId())) {
            throw new IllegalArgumentException("Device already exists with ID: " + device.getDeviceId());
        }
        return networkDeviceRepository.save(device);
    }

    @Transactional
    public PortDocument configurePort(UUID deviceId, PortDocument port) {
        NetworkDeviceDocument device = networkDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));
        
        if (portRepository.existsByDeviceAndPortNumber(device, port.getPortNumber())) {
            throw new IllegalArgumentException("Port already exists: " + port.getPortNumber());
        }
        
        port.setDevice(device);
        return portRepository.save(port);
    }

    @Transactional
    public QoSPolicyDocument createQoSPolicy(QoSPolicyDocument policy) {
        return qosPolicyRepository.save(policy);
    }

    @Transactional(readOnly = true)
    public NetworkDeviceDocument getDevice(UUID id) {
        return networkDeviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<PortDocument> getDevicePorts(UUID deviceId) {
        NetworkDeviceDocument device = getDevice(deviceId);
        return portRepository.findByDevice(device);
    }

    @Transactional(readOnly = true)
    public List<QoSPolicyDocument> getPolicies() {
        return qosPolicyRepository.findAll();
    }

    @Transactional
    public void updateDeviceStatus(UUID id, boolean isActive) {
        NetworkDeviceDocument device = getDevice(id);
        device.setIsActive(isActive);
        device.setLastSeen(LocalDateTime.now());
        networkDeviceRepository.save(device);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void checkDeviceStatus() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        List<NetworkDeviceDocument> inactiveDevices = networkDeviceRepository.findInactiveDevices(threshold);
        
        for (NetworkDeviceDocument device : inactiveDevices) {
            device.setIsActive(false);
            networkDeviceRepository.save(device);
        }
    }

    @Transactional
    public void deleteDevice(UUID id) {
        if (!networkDeviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Device not found with id: " + id);
        }
        networkDeviceRepository.deleteById(id);
    }

    @Transactional
    public void deletePort(UUID id) {
        if (!portRepository.existsById(id)) {
            throw new EntityNotFoundException("Port not found with id: " + id);
        }
        portRepository.deleteById(id);
    }

    @Transactional
    public void deleteQoSPolicy(UUID id) {
        if (!qosPolicyRepository.existsById(id)) {
            throw new EntityNotFoundException("QoS policy not found with id: " + id);
        }
        qosPolicyRepository.deleteById(id);
    }

    @Transactional
    public NetworkConfigDocument getConfiguration(UUID id) {
        return networkConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
    }

    @Transactional
    public List<NetworkConfigDocument> getAllConfigurations() {
        return networkConfigurationRepository.findAll();
    }

    @Transactional
    public NetworkConfigDocument createConfiguration(NetworkConfigDocument configuration) {
        configuration.setCreatedAt(LocalDateTime.now());
        return networkConfigurationRepository.save(configuration);
    }

    @Transactional
    public NetworkConfigDocument updateConfiguration(UUID id, NetworkConfigDocument configuration) {
        NetworkConfigDocument existingConfig = getConfiguration(id);
        existingConfig.setName(configuration.getName());
        existingConfig.setDescription(configuration.getDescription());
        existingConfig.setParameters(configuration.getParameters());
        existingConfig.setUpdatedAt(LocalDateTime.now());
        return networkConfigurationRepository.save(existingConfig);
    }

    @Transactional
    public void deleteConfiguration(UUID id) {
        networkConfigurationRepository.deleteById(id);
    }

    @Transactional
    public NetworkConfigDocument applyConfiguration(UUID configId, UUID deviceId) {
        NetworkConfigDocument config = getConfiguration(configId);
        NetworkDeviceDocument device = networkDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        // Apply configuration to device
        config.setLastApplied(LocalDateTime.now());
        config.setAppliedTo(device);
        return networkConfigurationRepository.save(config);
    }

    @Transactional
    public List<NetworkConfigDocument> getConfigurationsByDevice(UUID deviceId) {
        NetworkDeviceDocument device = networkDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        return networkConfigurationRepository.findByAppliedTo(device);
    }

    @Transactional
    public List<NetworkConfigDocument> searchConfigurations(String name, String description) {
        return networkConfigurationRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(name, description);
    }
}
