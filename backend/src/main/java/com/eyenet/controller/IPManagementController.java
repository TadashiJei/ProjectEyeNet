package com.eyenet.controller;

import com.eyenet.model.entity.IPAssignment;
import com.eyenet.model.entity.IPRange;
import com.eyenet.service.IPManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ip")
@RequiredArgsConstructor
public class IPManagementController {
    private final IPManagementService ipManagementService;

    @PostMapping("/ranges")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<IPRange> createIPRange(@Valid @RequestBody IPRange ipRange) {
        return ResponseEntity.ok(ipManagementService.createIPRange(ipRange));
    }

    @GetMapping("/ranges/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<IPRange> getIPRange(@PathVariable UUID id) {
        return ResponseEntity.ok(ipManagementService.getIPRange(id));
    }

    @GetMapping("/ranges/department/{departmentId}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<List<IPRange>> getIPRangesByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(ipManagementService.getIPRangesByDepartment(departmentId));
    }

    @PostMapping("/assignments")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<IPAssignment> assignIP(@Valid @RequestBody IPAssignment ipAssignment) {
        return ResponseEntity.ok(ipManagementService.assignIP(ipAssignment));
    }

    @DeleteMapping("/assignments/{id}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<Void> releaseIP(@PathVariable UUID id) {
        ipManagementService.releaseIP(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assignments/department/{departmentId}")
    @PreAuthorize("hasRole('NETWORK_ADMIN')")
    public ResponseEntity<List<IPAssignment>> getIPAssignmentsByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(ipManagementService.getIPAssignmentsByDepartment(departmentId));
    }
}
