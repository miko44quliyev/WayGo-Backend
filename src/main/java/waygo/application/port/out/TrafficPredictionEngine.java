package waygo.application.port.out;

import waygo.domain.model.HistoricalPattern;
import waygo.domain.model.RoadSegment;
import waygo.domain.model.TrafficForecast;
import waygo.domain.model.TrafficSnapshot;

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
