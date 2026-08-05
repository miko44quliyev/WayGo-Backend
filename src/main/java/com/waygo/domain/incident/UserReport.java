package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.domain.incident;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record UserReport(UUID userId, UUID segmentId, ReportType type, String description, Instant createdAt) {

    public UserReport {
        userId = Objects.requireNonNull(userId, "userId");
        segmentId = Objects.requireNonNull(segmentId, "segmentId");
        type = Objects.requireNonNull(type, "type");
        description = Objects.requireNonNull(description, "description");
        createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }
}
