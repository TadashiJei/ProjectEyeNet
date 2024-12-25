package com.eyenet.controller;

import com.eyenet.model.document.*;
import com.eyenet.service.AnalyticsEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsEngineController {
    private final AnalyticsEngineService analyticsEngineService;

    @GetMapping("/department/{departmentId}")
    public Flux<DepartmentAnalytics> getDepartmentAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsEngineService.analyzeDepartmentPerformance(departmentId, start, end);
    }

    @GetMapping("/traffic/{departmentId}")
    public Flux<TrafficAnalytics> getTrafficAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsEngineService.analyzeTrafficPatterns(departmentId, start, end);
    }

    @GetMapping("/dashboard/{departmentId}")
    public ResponseEntity<Mono<Map<String, Object>>> getDashboardMetrics(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(analyticsEngineService.generateDashboardMetrics(departmentId));
    }

    @GetMapping("/website-access/{departmentId}")
    public Flux<WebsiteAccessLog> getWebsiteAccessAnalytics(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsEngineService.analyzeWebsiteAccess(departmentId, start, end);
    }
}
