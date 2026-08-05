package com.waygo.infrastructure.prediction;

import com.waygo.domain.model.*;

import com.waygo.domain.model.Coordinate;
import com.waygo.domain.model.HistoricalPattern;
import com.waygo.domain.model.RoadSegment;
import com.waygo.domain.model.TrafficForecast;
import com.waygo.domain.model.TrafficSnapshot;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticalTrafficPredictionEngineTest {

    private final StatisticalTrafficPredictionEngine engine = new StatisticalTrafficPredictionEngine();

    @Test
    void forecastShouldUseHistoricalPatternAndLatestSnapshot() {
        RoadSegment segment = segment();
        TrafficForecast forecast = engine.forecast(
                segment,
                Optional.of(new TrafficSnapshot(segment.id(), Instant.parse("2026-08-05T00:00:00Z"), 20.0, 60)),
                Optional.of(new HistoricalPattern(segment.id(), DayOfWeek.MONDAY, 8, 30.0, 5.0)),
                DayOfWeek.MONDAY,
                8,
                10
        );

        assertEquals(23.5, forecast.predictedSpeedKmh());
        assertEquals(53, forecast.predictedCongestionLevel());
        assertTrue(forecast.explanation().contains("historical behavior"));
    }

    @Test
    void forecastShouldFallbackWhenHistoricalPatternMissing() {
        RoadSegment segment = segment();
        TrafficForecast forecast = engine.forecast(
                segment,
                Optional.of(new TrafficSnapshot(segment.id(), Instant.parse("2026-08-05T00:00:00Z"), 25.0, 50)),
                Optional.empty(),
                DayOfWeek.SATURDAY,
                14,
                0
        );

        assertEquals(27.5, forecast.predictedSpeedKmh());
        assertTrue(forecast.explanation().contains("fallback"));
    }

    private RoadSegment segment() {
        return new RoadSegment(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Test Segment",
                List.of(new Coordinate(40.0, 49.0), new Coordinate(40.01, 49.02)),
                "Test Zone"
        );
    }
}
