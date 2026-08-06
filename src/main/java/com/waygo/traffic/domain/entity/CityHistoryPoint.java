package com.waygo.traffic.domain.entity;

import java.time.Instant;

public record CityHistoryPoint(Instant bucketStart, double averageSpeedKmh, int averageCongestionLevel) {
}
