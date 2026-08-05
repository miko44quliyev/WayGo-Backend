package waygo.domain.model;

public record WeatherSnapshot(
        String locationName,
        double latitude,
        double longitude,
        double temperatureC,
        double windSpeedKmh,
        double precipitationMm,
        String condition,
        int trafficImpactPercent,
        String source
) {
}
