package com.waygo.domain.traffic;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record GpsPing(UUID deviceId, double latitude, double longitude, Instant timestamp, double speedKmh) {

    public GpsPing {
        deviceId = Objects.requireNonNull(deviceId, "deviceId");
        timestamp = Objects.requireNonNull(timestamp, "timestamp");
    }
}
