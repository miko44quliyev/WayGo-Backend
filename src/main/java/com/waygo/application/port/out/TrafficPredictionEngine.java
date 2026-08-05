package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.out;

import com.waygo.domain.traffic.HistoricalPattern;
import com.waygo.domain.traffic.RoadSegment;
import com.waygo.domain.traffic.TrafficForecast;
import com.waygo.domain.traffic.TrafficSnapshot;

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
