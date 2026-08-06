package com.waygo.traffic.infrastructure.adapter.inbound.http;

import com.waygo.report.domain.entity.UserReport;
import com.waygo.traffic.application.service.AdminIncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/reports")
public class AdminController {

    private final AdminIncidentService adminIncidentService;

    public AdminController(AdminIncidentService adminIncidentService) {
        this.adminIncidentService = adminIncidentService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserReport>> getPendingReports() {
        return ResponseEntity.ok(adminIncidentService.getPendingReports());
    }

    @PostMapping("/{reportId}/approve")
    public ResponseEntity<Void> approveReport(@PathVariable UUID reportId) {
        adminIncidentService.approveReport(reportId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reportId}/reject")
    public ResponseEntity<Void> rejectReport(@PathVariable UUID reportId) {
        adminIncidentService.rejectReport(reportId);
        return ResponseEntity.ok().build();
    }
}
