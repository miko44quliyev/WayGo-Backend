package com.waygo.domain.model;

import java.time.Instant;
import java.util.List;

public record CityStats(
        double averageSpeedKmh,
        double congestionPercent,
        long activeVehiclesCount,
        List<CityHistoryPoint> last24Hours,
        Instant generatedAt
) {
}
