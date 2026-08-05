package com.waygo.domain.traffic;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TrafficAnomaly(UUID segmentId, Instant detectedAt, double zScore, AnomalyStatus status, String description) {

    public TrafficAnomaly {
        segmentId = Objects.requireNonNull(segmentId, "segmentId");
        detectedAt = Objects.requireNonNull(detectedAt, "detectedAt");
        status = Objects.requireNonNull(status, "status");
        description = Objects.requireNonNull(description, "description");
    }
}
