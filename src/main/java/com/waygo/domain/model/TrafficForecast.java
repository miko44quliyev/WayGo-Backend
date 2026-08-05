package com.waygo.domain.model;

import java.time.DayOfWeek;
import java.util.Objects;
import java.util.UUID;

public record TrafficForecast(
        UUID segmentId,
        String segmentName,
        DayOfWeek dayOfWeek,
        int hour,
        double predictedSpeedKmh,
        int predictedCongestionLevel,
        double reliabilityScore,
        String explanation
) {

    public TrafficForecast {
        segmentId = Objects.requireNonNull(segmentId, "segmentId");
        segmentName = Objects.requireNonNull(segmentName, "segmentName");
        dayOfWeek = Objects.requireNonNull(dayOfWeek, "dayOfWeek");
        explanation = Objects.requireNonNull(explanation, "explanation");
    }
}
