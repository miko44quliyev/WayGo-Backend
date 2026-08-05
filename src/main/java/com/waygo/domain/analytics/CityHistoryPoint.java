package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.domain.analytics;

import java.time.Instant;

public record CityHistoryPoint(Instant bucketStart, double averageSpeedKmh, int averageCongestionLevel) {
}
