package waygo.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TrafficSnapshot(UUID segmentId, Instant timestamp, double averageSpeedKmh, int congestionLevel) {

    public TrafficSnapshot {
        segmentId = Objects.requireNonNull(segmentId, "segmentId");
        timestamp = Objects.requireNonNull(timestamp, "timestamp");
    }
}
