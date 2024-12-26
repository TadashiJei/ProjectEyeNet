package com.eyenet.service;

import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.model.entity.Port;
import com.eyenet.model.entity.QoSPolicy;
import com.eyenet.repository.jpa.NetworkDeviceRepository;
import com.eyenet.repository.jpa.PortRepository;
import com.eyenet.repository.jpa.QoSPolicyRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NetworkConfigurationService {
    private final NetworkDeviceRepository deviceRepository;
    private final PortRepository portRepository;
    private final QoSPolicyRepository qosPolicyRepository;

    @Transactional
    public NetworkDevice registerDevice(NetworkDevice device) {
        if (deviceRepository.existsByDeviceId(device.getDeviceId())) {
            throw new IllegalArgumentException("Device already exists with ID: " + device.getDeviceId());
        }
        return deviceRepository.save(device);
    }

    @Transactional
    public Port configurePort(UUID deviceId, Port port) {
        NetworkDevice device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + deviceId));
        
        if (portRepository.existsByDeviceAndPortNumber(device, port.getPortNumber())) {
            throw new IllegalArgumentException("Port already exists: " + port.getPortNumber());
        }
        
        port.setDevice(device);
        return portRepository.save(port);
    }

    @Transactional
    public QoSPolicy createQoSPolicy(QoSPolicy policy) {
        if (qosPolicyRepository.existsByNameAndDepartment(policy.getName(), policy.getDepartment())) {
            throw new IllegalArgumentException("QoS policy already exists with name: " + policy.getName());
        }
        return qosPolicyRepository.save(policy);
    }

    @Transactional(readOnly = true)
    public NetworkDevice getDevice(UUID id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Port> getDevicePorts(UUID deviceId) {
        NetworkDevice device = getDevice(deviceId);
        return portRepository.findByDevice(device);
    }

    @Transactional(readOnly = true)
    public List<QoSPolicy> getDepartmentPolicies(UUID departmentId) {
        return qosPolicyRepository.findByDepartment(Department.builder().id(departmentId).build());
    }

    @Transactional
    public void updateDeviceStatus(UUID id, boolean isActive) {
        NetworkDevice device = getDevice(id);
        device.setIsActive(isActive);
        device.setLastSeen(LocalDateTime.now());
        deviceRepository.save(device);
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void checkDeviceStatus() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        List<NetworkDevice> inactiveDevices = deviceRepository.findInactiveDevices(threshold);
        
        for (NetworkDevice device : inactiveDevices) {
            device.setIsActive(false);
            deviceRepository.save(device);
        }
    }

    @Transactional
    public void deleteDevice(UUID id) {
        if (!deviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Device not found with id: " + id);
        }
        deviceRepository.deleteById(id);
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
}
