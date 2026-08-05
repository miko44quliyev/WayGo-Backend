package org.example.waygo.domain.model;

import java.time.DayOfWeek;
import java.util.Objects;
import java.util.UUID;

public record HistoricalPattern(UUID segmentId, DayOfWeek dayOfWeek, int hour, double averageSpeedKmh, double standardDeviation) {

    public HistoricalPattern {
        segmentId = Objects.requireNonNull(segmentId, "segmentId");
        dayOfWeek = Objects.requireNonNull(dayOfWeek, "dayOfWeek");
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("hour must be between 0 and 23");
        }
    }
}
