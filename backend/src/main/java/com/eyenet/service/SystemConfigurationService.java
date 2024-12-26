package com.eyenet.service;

import com.eyenet.model.document.SystemConfigurationDocument;
import com.eyenet.model.document.SystemLogDocument;
import com.eyenet.repository.mongodb.SystemConfigurationRepository;
import com.eyenet.repository.mongodb.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SystemConfigurationService {
    private final SystemConfigurationRepository systemConfigurationRepository;
    private final SystemLogRepository systemLogRepository;
    private final long startTime = System.currentTimeMillis();
    private boolean maintenanceMode = false;

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
        status.put("status", maintenanceMode ? "maintenance" : "running");
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

    public String createBackup() {
        String backupId = UUID.randomUUID().toString();
        // Implement backup logic here
        // For example, create a snapshot of the database
        createSystemLog("INFO", "System backup created: " + backupId, "BACKUP");
        return backupId;
    }

    public void restoreBackup(String backupId) {
        // Implement restore logic here
        // For example, restore from a database snapshot
        createSystemLog("INFO", "System restored from backup: " + backupId, "BACKUP");
    }

    public List<SystemLogDocument> getSystemLogs(String level, String component) {
        List<SystemLogDocument> logs = systemLogRepository.findAll();
        
        return logs.stream()
                .filter(log -> level == null || log.getLevel().equals(level))
                .filter(log -> component == null || log.getSource().equals(component))
                .collect(Collectors.toList());
    }

    public void setMaintenanceMode(boolean enabled) {
        this.maintenanceMode = enabled;
        String message = enabled ? "System entered maintenance mode" : "System exited maintenance mode";
        createSystemLog("INFO", message, "SYSTEM");
    }

    public SystemLogDocument createSystemLog(String level, String message, String source) {
        SystemLogDocument log = SystemLogDocument.builder()
                .id(UUID.randomUUID())
                .level(level)
                .message(message)
                .source(source)
                .timestamp(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        return systemLogRepository.save(log);
    }
}
