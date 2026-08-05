package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.in;

import com.waygo.domain.traffic.ReportType;

import java.time.Instant;
import java.util.UUID;

public record SubmitReportCommand(UUID userId, UUID segmentId, ReportType type, String description, Instant createdAt) {
}
