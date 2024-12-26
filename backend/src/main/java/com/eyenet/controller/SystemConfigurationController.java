package com.eyenet.controller;

import com.eyenet.model.entity.SystemConfiguration;
import com.eyenet.model.entity.SystemLog;
import com.eyenet.service.SystemConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class SystemConfigurationController {
    private final SystemConfigurationService systemConfigService;

    @PostMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfiguration> createSystemConfig(@Valid @RequestBody SystemConfiguration config) {
        return ResponseEntity.ok(systemConfigService.createConfiguration(config));
    }

    @GetMapping("/config/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfiguration> getSystemConfig(@PathVariable UUID id) {
        return ResponseEntity.ok(systemConfigService.getSystemConfiguration(id));
    }

    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SystemConfiguration>> getAllConfigs() {
        return ResponseEntity.ok(systemConfigService.getAllConfigurations());
    }

    @PutMapping("/config/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfiguration> updateSystemConfig(
            @PathVariable UUID id,
            @Valid @RequestBody SystemConfiguration config) {
        return ResponseEntity.ok(systemConfigService.updateConfiguration(id, config));
    }

    @DeleteMapping("/config/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSystemConfig(@PathVariable UUID id) {
        systemConfigService.deleteConfiguration(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        return ResponseEntity.ok(systemConfigService.getSystemStatus());
    }

    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createBackup() {
        return ResponseEntity.ok(systemConfigService.createBackup());
    }

    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restoreBackup(@RequestParam String backupId) {
        systemConfigService.restoreBackup(backupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SystemLog>> getSystemLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String component) {
        return ResponseEntity.ok(systemConfigService.getSystemLogs(level, component));
    }

    @PostMapping("/maintenance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> toggleMaintenanceMode(@RequestParam boolean enabled) {
        systemConfigService.setMaintenanceMode(enabled);
        return ResponseEntity.ok().build();
    }
}
