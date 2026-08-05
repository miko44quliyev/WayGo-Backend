package com.waygo.application.port.out;

import com.waygo.domain.model.*;






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
