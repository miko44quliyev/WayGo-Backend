package com.waygo.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record TrafficMapView(List<TrafficMapEntry> segments, Instant generatedAt) {

    public TrafficMapView {
        segments = List.copyOf(Objects.requireNonNull(segments, "segments"));
        generatedAt = Objects.requireNonNull(generatedAt, "generatedAt");
    }
}
