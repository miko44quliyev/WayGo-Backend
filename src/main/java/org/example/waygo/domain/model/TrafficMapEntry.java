package org.example.waygo.domain.model;

import java.util.List;
import java.util.UUID;

public record TrafficMapEntry(
        UUID segmentId,
        String segmentName,
        String zone,
        List<Coordinate> coordinates,
        double currentSpeedKmh,
        int currentCongestionLevel,
        double predictedSpeedKmh,
        int predictedCongestionLevel,
        boolean anomalyDetected
) {
}
