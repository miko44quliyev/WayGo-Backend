package com.waygo.application.port.in;

import com.waygo.domain.model.*;



import java.time.Instant;
import java.util.UUID;

public record SubmitReportCommand(UUID userId, UUID segmentId, ReportType type, String description, Instant createdAt) {
}
