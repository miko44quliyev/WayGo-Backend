package com.waygo.traffic.application.port.outbound;

import com.waygo.traffic.domain.entity.HistoricalPattern;
import com.waygo.traffic.domain.entity.RoadSegment;
import com.waygo.traffic.domain.entity.TrafficForecast;
import com.waygo.traffic.domain.entity.TrafficSnapshot;







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
