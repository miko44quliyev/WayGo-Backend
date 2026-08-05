package waygo.application.service;

import waygo.application.port.in.GetTrafficMapUseCase;
import waygo.application.port.out.HistoricalPatternRepository;
import waygo.application.port.out.RoadSegmentRepository;
import waygo.application.port.out.TrafficAnomalyRepository;
import waygo.application.port.out.TrafficPredictionEngine;
import waygo.application.port.out.TrafficSnapshotRepository;
import waygo.domain.model.RoadSegment;
import waygo.domain.model.TrafficForecast;
import waygo.domain.model.TrafficMapEntry;
import waygo.domain.model.TrafficMapView;
import waygo.domain.model.TrafficSnapshot;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
public class GetTrafficMapService implements GetTrafficMapUseCase {

    private static final ZoneId BAKU_ZONE = ZoneId.of("Asia/Baku");

    private final RoadSegmentRepository roadSegmentRepository;
    private final TrafficSnapshotRepository trafficSnapshotRepository;
    private final HistoricalPatternRepository historicalPatternRepository;
    private final TrafficAnomalyRepository trafficAnomalyRepository;
    private final TrafficPredictionEngine trafficPredictionEngine;

    public GetTrafficMapService(
            RoadSegmentRepository roadSegmentRepository,
            TrafficSnapshotRepository trafficSnapshotRepository,
            HistoricalPatternRepository historicalPatternRepository,
            TrafficAnomalyRepository trafficAnomalyRepository,
            TrafficPredictionEngine trafficPredictionEngine
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.trafficSnapshotRepository = trafficSnapshotRepository;
        this.historicalPatternRepository = historicalPatternRepository;
        this.trafficAnomalyRepository = trafficAnomalyRepository;
        this.trafficPredictionEngine = trafficPredictionEngine;
    }

    @Override
    public TrafficMapView handle() {
        Instant now = Instant.now();
        var currentDateTime = now.atZone(BAKU_ZONE);
        List<TrafficMapEntry> entries = roadSegmentRepository.findAll().stream()
                .map(segment -> buildEntry(segment, currentDateTime.getDayOfWeek(), currentDateTime.getHour()))
                .toList();
        return new TrafficMapView(entries, now);
    }

    private TrafficMapEntry buildEntry(RoadSegment segment, java.time.DayOfWeek dayOfWeek, int hour) {
        TrafficSnapshot snapshot = trafficSnapshotRepository.findLatestBySegmentId(segment.id())
                .orElseGet(() -> new TrafficSnapshot(segment.id(), Instant.now(), 35.0, 30));
        TrafficForecast forecast = trafficPredictionEngine.forecast(
                segment,
                java.util.Optional.of(snapshot),
                historicalPatternRepository.findBySegmentIdAndDayOfWeekAndHour(segment.id(), dayOfWeek, hour),
                dayOfWeek,
                hour,
                historicalPatternRepository.findBySegmentId(segment.id()).size()
        );

        boolean anomalyDetected = !trafficAnomalyRepository.findBySegmentId(segment.id()).isEmpty();
        return new TrafficMapEntry(
                segment.id(),
                segment.name(),
                segment.zone(),
                segment.coordinates(),
                snapshot.averageSpeedKmh(),
                snapshot.congestionLevel(),
                forecast.predictedSpeedKmh(),
                forecast.predictedCongestionLevel(),
                anomalyDetected
        );
    }
}
