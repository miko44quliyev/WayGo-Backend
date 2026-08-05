package waygo.application.port.in;

import waygo.domain.model.ReportType;

import java.time.Instant;
import java.util.UUID;

public record SubmitReportCommand(UUID userId, UUID segmentId, ReportType type, String description, Instant createdAt) {
}
