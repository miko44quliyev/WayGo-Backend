package org.example.waygo.domain.model;

import java.util.UUID;

public record TrafficMapEntry(
        UUID segmentId,
        String segmentName,
        String zone,
        double currentSpeedKmh,
        int currentCongestionLevel,
        double predictedSpeedKmh,
        int predictedCongestionLevel,
        boolean anomalyDetected
) {
}
