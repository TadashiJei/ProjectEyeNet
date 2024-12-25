package com.eyenet.controller;

import com.eyenet.model.document.*;
import com.eyenet.service.AnalyticsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics/reports")
@RequiredArgsConstructor
public class AnalyticsReportController {
    private final AnalyticsReportService analyticsReportService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnalyticsReport> generateReport(@Valid @RequestBody ReportConfig config) {
        return ResponseEntity.ok(analyticsReportService.generateReport(config));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AnalyticsReport>> getDepartmentReports(
            @PathVariable UUID departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsReportService.getDepartmentReports(departmentId, start, end));
    }

    @GetMapping("/traffic")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrafficAnalytics>> getTrafficReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsReportService.getTrafficReports(start, end));
    }

    @GetMapping("/performance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PerformanceMetrics>> getPerformanceReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsReportService.getPerformanceReports(start, end));
    }

    @GetMapping("/security")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SecurityMetrics>> getSecurityReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(analyticsReportService.getSecurityReports(start, end));
    }

    @GetMapping("/custom")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getCustomReport(
            @RequestParam String reportType,
            @RequestParam Map<String, String> parameters) {
        return ResponseEntity.ok(analyticsReportService.getCustomReport(reportType, parameters));
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportSchedule> scheduleReport(@Valid @RequestBody ReportScheduleConfig config) {
        return ResponseEntity.ok(analyticsReportService.scheduleReport(config));
    }

    @GetMapping("/scheduled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportSchedule>> getScheduledReports() {
        return ResponseEntity.ok(analyticsReportService.getScheduledReports());
    }

    @DeleteMapping("/scheduled/{scheduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteScheduledReport(@PathVariable UUID scheduleId) {
        analyticsReportService.deleteScheduledReport(scheduleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/export/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportReport(
            @PathVariable UUID reportId,
            @RequestParam(defaultValue = "PDF") String format) {
        return ResponseEntity.ok(analyticsReportService.exportReport(reportId, format));
    }
}
