package com.waygo.traffic.application.dto;

import com.waygo.report.domain.valueobject.ReportType;




import java.time.Instant;
import java.util.UUID;

public record SubmitReportCommand(UUID userId, UUID segmentId, ReportType type, String description, Instant createdAt, Double latitude, Double longitude) {
}
