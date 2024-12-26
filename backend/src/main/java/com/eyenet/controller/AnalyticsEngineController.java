package com.eyenet.controller;

import com.eyenet.model.document.*;
import com.eyenet.service.AnalyticsEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsEngineController {
    private final AnalyticsEngineService analyticsService;

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<DepartmentAnalyticsDocument> generateDepartmentAnalytics(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(analyticsService.generateDepartmentAnalytics(departmentId));
    }

    @GetMapping("/department/{departmentId}/history")
    public ResponseEntity<List<DepartmentAnalyticsDocument>> getDepartmentAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getDepartmentAnalytics(departmentId, start, end));
    }

    @GetMapping("/traffic/{departmentId}")
    public ResponseEntity<List<TrafficAnalyticsDocument>> getTrafficAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getTrafficAnalytics(departmentId, start, end));
    }

    @GetMapping("/access-logs/{departmentId}")
    public ResponseEntity<List<WebsiteAccessLogDocument>> getAccessLogs(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getAccessLogs(departmentId, start, end));
    }

    @GetMapping("/department/{departmentId}/summary")
    public ResponseEntity<DepartmentAnalyticsDocument> getDepartmentSummary(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(analyticsService.getDepartmentSummary(departmentId));
    }

    @GetMapping("/traffic/{departmentId}/summary")
    public ResponseEntity<TrafficAnalyticsDocument> getTrafficSummary(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(analyticsService.getTrafficSummary(departmentId));
    }
}
