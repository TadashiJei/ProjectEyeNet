package com.eyenet.controller;

import com.eyenet.model.document.DepartmentAnalyticsDocument;
import com.eyenet.model.document.TrafficAnalyticsDocument;
import com.eyenet.model.document.WebsiteAccessLogDocument;
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
    public ResponseEntity<List<DepartmentAnalyticsDocument>> getDepartmentAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getDepartmentAnalytics(departmentId, start, end));
    }

    @GetMapping("/traffic/{deviceId}")
    public ResponseEntity<List<TrafficAnalyticsDocument>> getTrafficAnalytics(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String category) {
        if (category != null) {
            return ResponseEntity.ok(analyticsService.getTrafficAnalyticsByCategory(deviceId, category, start, end));
        }
        return ResponseEntity.ok(analyticsService.getTrafficAnalytics(deviceId, start, end));
    }

    @GetMapping("/access-logs/{deviceId}")
    public ResponseEntity<List<WebsiteAccessLogDocument>> getAccessLogs(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String category) {
        if (category != null) {
            return ResponseEntity.ok(analyticsService.getAccessLogsByCategory(deviceId, category, start, end));
        }
        return ResponseEntity.ok(analyticsService.getAccessLogs(deviceId, start, end));
    }

    @GetMapping("/summary/department/{departmentId}")
    public ResponseEntity<DepartmentAnalyticsDocument> getDepartmentSummary(
            @PathVariable UUID departmentId) {
        return ResponseEntity.ok(analyticsService.getDepartmentSummary(departmentId));
    }

    @GetMapping("/summary/traffic/{deviceId}")
    public ResponseEntity<TrafficAnalyticsDocument> getTrafficSummary(
            @PathVariable UUID deviceId) {
        return ResponseEntity.ok(analyticsService.getTrafficSummary(deviceId));
    }
}
