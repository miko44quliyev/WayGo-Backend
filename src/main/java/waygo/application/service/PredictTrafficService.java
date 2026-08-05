package waygo.application.service;

import waygo.application.port.in.PredictTrafficQuery;
import waygo.application.port.in.PredictTrafficUseCase;
import waygo.application.port.out.HistoricalPatternRepository;
import waygo.application.port.out.RoadSegmentRepository;
import waygo.application.port.out.TrafficPredictionEngine;
import waygo.application.port.out.TrafficSnapshotRepository;
import waygo.domain.model.TrafficForecast;
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
