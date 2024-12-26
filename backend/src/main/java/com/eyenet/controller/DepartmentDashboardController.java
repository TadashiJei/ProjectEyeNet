package com.eyenet.controller;

import com.eyenet.model.document.*;
import com.eyenet.service.DepartmentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentDashboardController {
    private final DepartmentDashboardService dashboardService;

    @GetMapping("/{departmentId}/metrics")
    public ResponseEntity<Map<String, Object>> getDepartmentMetrics(
            @PathVariable UUID departmentId) {
        return ResponseEntity.ok(dashboardService.getDepartmentMetrics(departmentId));
    }

    @GetMapping("/{departmentId}/performance")
    public ResponseEntity<List<PerformanceMetricsDocument>> getPerformanceMetrics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getPerformanceMetrics(departmentId, start, end));
    }

    @GetMapping("/{departmentId}/network")
    public ResponseEntity<List<NetworkMetricsDocument>> getNetworkMetrics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getNetworkMetrics(departmentId, start, end));
    }

    @GetMapping("/{departmentId}/security")
    public ResponseEntity<List<SecurityMetricsDocument>> getSecurityMetrics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getSecurityMetrics(departmentId, start, end));
    }

    @GetMapping("/{departmentId}/traffic")
    public ResponseEntity<List<TrafficAnalyticsDocument>> getTrafficAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getTrafficAnalytics(departmentId, start, end));
    }

    @GetMapping("/{departmentId}/alerts")
    public ResponseEntity<List<AlertDocument>> getDepartmentAlerts(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getDepartmentAlerts(departmentId, start, end));
    }
}
