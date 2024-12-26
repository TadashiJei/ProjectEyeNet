package com.eyenet.controller;

import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.model.entity.Port;
import com.eyenet.model.entity.QoSPolicy;
import com.eyenet.service.NetworkConfigurationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/network-config")
@RequiredArgsConstructor
public class NetworkConfigurationController {
    private final NetworkConfigurationService networkConfigService;

    @PostMapping("/devices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NetworkDevice> registerDevice(@Valid @RequestBody NetworkDevice device) {
        return ResponseEntity.ok(networkConfigService.registerDevice(device));
    }

    @PostMapping("/devices/{deviceId}/ports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Port> configurePort(
            @PathVariable UUID deviceId,
            @Valid @RequestBody Port port) {
        return ResponseEntity.ok(networkConfigService.configurePort(deviceId, port));
    }

    @PostMapping("/qos-policies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QoSPolicy> createQoSPolicy(@Valid @RequestBody QoSPolicy policy) {
        return ResponseEntity.ok(networkConfigService.createQoSPolicy(policy));
    }

    @GetMapping("/devices/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<NetworkDevice> getDevice(@PathVariable UUID id) {
        return ResponseEntity.ok(networkConfigService.getDevice(id));
    }

    @GetMapping("/devices/{deviceId}/ports")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Port>> getDevicePorts(@PathVariable UUID deviceId) {
        return ResponseEntity.ok(networkConfigService.getDevicePorts(deviceId));
    }

    @GetMapping("/qos-policies/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<QoSPolicy>> getDepartmentPolicies(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(networkConfigService.getDepartmentPolicies(departmentId));
    }

    @PatchMapping("/devices/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateDeviceStatus(
            @PathVariable UUID id,
            @RequestParam boolean isActive) {
        networkConfigService.updateDeviceStatus(id, isActive);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/devices/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        networkConfigService.deleteDevice(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ports/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePort(@PathVariable UUID id) {
        networkConfigService.deletePort(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/qos-policies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQoSPolicy(@PathVariable UUID id) {
        networkConfigService.deleteQoSPolicy(id);
        return ResponseEntity.ok().build();
    }
}
