package com.eyenet.controller;

import com.eyenet.model.document.*;
import com.eyenet.service.DepartmentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/department/dashboard")
@RequiredArgsConstructor
public class DepartmentDashboardController {
    private final DepartmentDashboardService dashboardService;

    @GetMapping("/network-usage/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<NetworkUsageStats> getNetworkUsage(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getNetworkUsage(departmentId, start, end));
    }

    @GetMapping("/performance/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<List<PerformanceMetrics>> getPerformanceMetrics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getPerformanceMetrics(departmentId, start, end));
    }

    @GetMapping("/website-access/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<List<WebsiteAccessLog>> getWebsiteAccess(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getWebsiteAccess(departmentId, start, end));
    }

    @GetMapping("/analytics/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<DepartmentAnalytics> getDepartmentAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getDepartmentAnalytics(departmentId, start, end));
    }

    @GetMapping("/summary/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<Map<String, Object>> getDashboardSummary(
            @PathVariable UUID departmentId) {
        return ResponseEntity.ok(dashboardService.getDashboardSummary(departmentId));
    }

    @GetMapping("/alerts/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<List<Alert>> getDepartmentAlerts(
            @PathVariable UUID departmentId,
            @RequestParam(required = false) Alert.Severity minSeverity) {
        return ResponseEntity.ok(dashboardService.getDepartmentAlerts(departmentId, minSeverity));
    }

    @GetMapping("/top-users/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<List<UserUsageStats>> getTopUsers(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getTopUsers(departmentId, start, end));
    }

    @GetMapping("/bandwidth-trends/{departmentId}")
    @PreAuthorize("@securityUtils.canAccessDepartment(authentication, #departmentId)")
    public ResponseEntity<List<BandwidthTrend>> getBandwidthTrends(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getBandwidthTrends(departmentId, start, end));
    }
}
