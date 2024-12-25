package com.eyenet.controller;

import com.eyenet.model.entity.IPAssignment;
import com.eyenet.model.entity.IPRange;
import com.eyenet.service.IPManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ip-management")
@RequiredArgsConstructor
public class IPManagementController {
    private final IPManagementService ipManagementService;

    @PostMapping("/ranges")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IPRange> createIPRange(@Valid @RequestBody IPRange ipRange) {
        return ResponseEntity.ok(ipManagementService.createIPRange(ipRange));
    }

    @GetMapping("/ranges/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<IPRange> getIPRange(@PathVariable UUID id) {
        return ResponseEntity.ok(ipManagementService.getIPRange(id));
    }

    @GetMapping("/ranges/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<IPRange>> getIPRangesByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(ipManagementService.getIPRangesByDepartment(departmentId));
    }

    @PostMapping("/ranges/{rangeId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IPAssignment> assignIP(
            @PathVariable UUID rangeId,
            @Valid @RequestBody IPAssignment assignment) {
        return ResponseEntity.ok(ipManagementService.assignIP(rangeId, assignment));
    }

    @PostMapping("/release/{ipAddress}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> releaseIP(@PathVariable String ipAddress) {
        ipManagementService.releaseIP(ipAddress);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assignments/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<IPAssignment>> getAssignmentsByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(ipManagementService.getAssignmentsByDepartment(departmentId));
    }

    @DeleteMapping("/ranges/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteIPRange(@PathVariable UUID id) {
        ipManagementService.deleteIPRange(id);
        return ResponseEntity.ok().build();
    }
}
