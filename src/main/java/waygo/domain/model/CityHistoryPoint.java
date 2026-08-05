package waygo.domain.model;

import java.time.Instant;

public record CityHistoryPoint(Instant bucketStart, double averageSpeedKmh, int averageCongestionLevel) {
}
