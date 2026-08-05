package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.service;

import com.waygo.application.port.in.PredictTrafficQuery;
import com.waygo.application.port.in.PredictTrafficUseCase;
import com.waygo.application.port.out.HistoricalPatternRepository;
import com.waygo.application.port.out.RoadSegmentRepository;
import com.waygo.application.port.out.TrafficPredictionEngine;
import com.waygo.application.port.out.TrafficSnapshotRepository;
import com.waygo.domain.traffic.TrafficForecast;
import org.springframework.stereotype.Service;

@Service
public class PredictTrafficService implements PredictTrafficUseCase {

    private final RoadSegmentRepository roadSegmentRepository;
    private final TrafficSnapshotRepository trafficSnapshotRepository;
    private final HistoricalPatternRepository historicalPatternRepository;
    private final TrafficPredictionEngine trafficPredictionEngine;

    public PredictTrafficService(
            RoadSegmentRepository roadSegmentRepository,
            TrafficSnapshotRepository trafficSnapshotRepository,
            HistoricalPatternRepository historicalPatternRepository,
            TrafficPredictionEngine trafficPredictionEngine
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.trafficSnapshotRepository = trafficSnapshotRepository;
        this.historicalPatternRepository = historicalPatternRepository;
        this.trafficPredictionEngine = trafficPredictionEngine;
    }

    @Override
    public TrafficForecast handle(PredictTrafficQuery query) {
        var segment = roadSegmentRepository.findById(query.segmentId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown road segment: " + query.segmentId()));
        var latestSnapshot = trafficSnapshotRepository.findLatestBySegmentId(segment.id());
        var pattern = historicalPatternRepository.findBySegmentIdAndDayOfWeekAndHour(segment.id(), query.dayOfWeek(), query.hour());
        int sampleCount = historicalPatternRepository.findBySegmentId(segment.id()).size();
        return trafficPredictionEngine.forecast(segment, latestSnapshot, pattern, query.dayOfWeek(), query.hour(), sampleCount);
    }
}
