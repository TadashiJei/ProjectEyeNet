package com.eyenet.controller;

import com.eyenet.model.entity.*;
import com.eyenet.service.SystemConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class SystemConfigurationController {
    private final SystemConfigurationService systemConfigService;

    @PostMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfig> createSystemConfig(@Valid @RequestBody SystemConfig config) {
        return ResponseEntity.ok(systemConfigService.createConfig(config));
    }

    @GetMapping("/config/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfig> getSystemConfig(@PathVariable String key) {
        return ResponseEntity.ok(systemConfigService.getConfig(key));
    }

    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SystemConfig>> getAllConfigs() {
        return ResponseEntity.ok(systemConfigService.getAllConfigs());
    }

    @PutMapping("/config/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfig> updateSystemConfig(
            @PathVariable String key,
            @Valid @RequestBody SystemConfig config) {
        return ResponseEntity.ok(systemConfigService.updateConfig(key, config));
    }

    @DeleteMapping("/config/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSystemConfig(@PathVariable String key) {
        systemConfigService.deleteConfig(key);
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
