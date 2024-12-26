package com.eyenet.service;

import com.eyenet.model.document.SystemConfigurationDocument;
import com.eyenet.model.document.SystemLogDocument;
import com.eyenet.repository.mongodb.SystemConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SystemConfigurationService {
    private final SystemConfigurationRepository systemConfigurationRepository;
    private final long startTime = System.currentTimeMillis();

    public SystemConfigurationDocument getSystemConfiguration(UUID id) {
        return systemConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("System configuration not found"));
    }

    public List<SystemConfigurationDocument> getAllConfigurations() {
        return systemConfigurationRepository.findAll();
    }

    public SystemConfigurationDocument createConfiguration(SystemConfigurationDocument config) {
        config.setId(UUID.randomUUID());
        config.setCreatedAt(LocalDateTime.now());
        config.setLastUpdated(LocalDateTime.now());
        return systemConfigurationRepository.save(config);
    }

    public SystemConfigurationDocument updateConfiguration(UUID id, SystemConfigurationDocument config) {
        SystemConfigurationDocument existingConfig = getSystemConfiguration(id);
        existingConfig.setMaxConcurrentConnections(config.getMaxConcurrentConnections());
        existingConfig.setMaxBandwidthPerUser(config.getMaxBandwidthPerUser());
        existingConfig.setDefaultQoSPolicy(config.getDefaultQoSPolicy());
        existingConfig.setMonitoringInterval(config.getMonitoringInterval());
        existingConfig.setAlertThreshold(config.getAlertThreshold());
        existingConfig.setLastUpdated(LocalDateTime.now());
        return systemConfigurationRepository.save(existingConfig);
    }

    public void deleteConfiguration(UUID id) {
        systemConfigurationRepository.deleteById(id);
    }

    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "running");
        status.put("uptime", System.currentTimeMillis() - startTime);
        status.put("memory", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        status.put("cpuUsage", getCpuUsage());
        status.put("threadCount", Thread.activeCount());
        status.put("timestamp", LocalDateTime.now());
        return status;
    }

    private double getCpuUsage() {
        // This is a simplified implementation
        // In a real system, you would use proper CPU monitoring
        return Math.random() * 100;
    }

    public SystemLogDocument createSystemLog(String level, String message, String source) {
        return SystemLogDocument.builder()
                .id(UUID.randomUUID())
                .level(level)
                .message(message)
                .source(source)
                .timestamp(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
