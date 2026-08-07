package com.waygo.traffic.infrastructure.adapter.inbound.http;

import com.waygo.report.domain.entity.UserReport;
import com.waygo.traffic.application.usecase.AdminIncidentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin/reports")
public class AdminController {

    private final AdminIncidentUseCase AdminIncidentUseCase;

    public AdminController(AdminIncidentUseCase AdminIncidentUseCase) {
        this.AdminIncidentUseCase = AdminIncidentUseCase;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserReport>> getPendingReports() {
        return ResponseEntity.ok(AdminIncidentUseCase.getPendingReports());
    }

    @PostMapping("/{reportId}/approve")
    public ResponseEntity<Void> approveReport(@PathVariable UUID reportId) {
        AdminIncidentUseCase.approveReport(reportId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reportId}/reject")
    public ResponseEntity<Void> rejectReport(@PathVariable UUID reportId) {
        AdminIncidentUseCase.rejectReport(reportId);
        return ResponseEntity.ok().build();
    }
}

