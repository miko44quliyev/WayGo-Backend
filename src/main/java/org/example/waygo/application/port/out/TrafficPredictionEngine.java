package org.example.waygo.application.port.out;

import org.example.waygo.domain.model.HistoricalPattern;
import org.example.waygo.domain.model.RoadSegment;
import org.example.waygo.domain.model.TrafficForecast;
import org.example.waygo.domain.model.TrafficSnapshot;

import java.time.DayOfWeek;
import java.util.Optional;

public interface TrafficPredictionEngine {

    TrafficForecast forecast(
            RoadSegment segment,
            Optional<TrafficSnapshot> latestSnapshot,
            Optional<HistoricalPattern> historicalPattern,
            DayOfWeek dayOfWeek,
            int hour,
            int sampleCount
    );
}
