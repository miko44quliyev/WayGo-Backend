package org.example.waygo.infrastructure.prediction;

import org.example.waygo.application.port.out.TrafficPredictionEngine;
import org.example.waygo.domain.model.HistoricalPattern;
import org.example.waygo.domain.model.RoadSegment;
import org.example.waygo.domain.model.TrafficForecast;
import org.example.waygo.domain.model.TrafficSnapshot;
import org.example.waygo.infrastructure.support.TrafficMath;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Optional;

@Component
public class StatisticalTrafficPredictionEngine implements TrafficPredictionEngine {

    @Override
    public TrafficForecast forecast(
            RoadSegment segment,
            Optional<TrafficSnapshot> latestSnapshot,
            Optional<HistoricalPattern> historicalPattern,
            DayOfWeek dayOfWeek,
            int hour,
            int sampleCount
    ) {
        double historicalSpeed = historicalPattern.map(HistoricalPattern::averageSpeedKmh)
                .orElse(latestSnapshot.map(TrafficSnapshot::averageSpeedKmh).orElse(35.0));

        double trendAdjustment = latestSnapshot
                .map(snapshot -> (historicalSpeed - snapshot.averageSpeedKmh()) * 0.25)
                .orElse(0.0);
        double peakHourAdjustment = isPeakHour(hour) ? -4.0 : 0.0;
        double weekdayAdjustment = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY ? 2.5 : 0.0;

        double predictedSpeed = TrafficMath.clamp(historicalSpeed - trendAdjustment + peakHourAdjustment + weekdayAdjustment, 5.0, 90.0);
        int predictedCongestion = TrafficMath.congestionFromSpeed(predictedSpeed);
        double reliability = TrafficMath.reliabilityScore(sampleCount, latestSnapshot.isPresent());

        String explanation = historicalPattern.isPresent()
                ? "Forecast based on historical behavior and the latest traffic snapshot"
                : "Forecast based on the latest snapshot and seeded fallback behavior";

        return new TrafficForecast(
                segment.id(),
                segment.name(),
                dayOfWeek,
                hour,
                predictedSpeed,
                predictedCongestion,
                reliability,
                explanation
        );
    }

    private boolean isPeakHour(int hour) {
        return hour == 8 || hour == 9 || hour == 18 || hour == 19;
    }
}
