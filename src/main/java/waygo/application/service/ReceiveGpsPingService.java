package waygo.application.service;

import waygo.application.port.in.GpsPingReceipt;
import waygo.application.port.in.ReceiveGpsPingCommand;
import waygo.application.port.in.ReceiveGpsPingUseCase;
import waygo.application.port.out.IncidentRealtimePublisher;
import waygo.application.port.out.GpsPingRepository;
import waygo.application.port.out.HistoricalPatternRepository;
import waygo.application.port.out.RoadSegmentRepository;
import waygo.application.port.out.TrafficAnomalyRepository;
import waygo.application.port.out.TrafficSnapshotRepository;
import waygo.domain.model.AnomalyStatus;
import waygo.domain.model.GpsPing;
import waygo.domain.model.HistoricalPattern;
import waygo.domain.model.RoadIncident;
import waygo.domain.model.RoadSegment;
import waygo.domain.model.TrafficAnomaly;
import waygo.domain.model.TrafficSnapshot;
import waygo.infrastructure.support.TrafficMath;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ReceiveGpsPingService implements ReceiveGpsPingUseCase {

    private static final ZoneId BAKU_ZONE = ZoneId.of("Asia/Baku");

    private final RoadSegmentRepository roadSegmentRepository;
    private final GpsPingRepository gpsPingRepository;
    private final TrafficSnapshotRepository trafficSnapshotRepository;
    private final HistoricalPatternRepository historicalPatternRepository;
    private final TrafficAnomalyRepository trafficAnomalyRepository;
    private final IncidentRealtimePublisher incidentRealtimePublisher;

    public ReceiveGpsPingService(
            RoadSegmentRepository roadSegmentRepository,
            GpsPingRepository gpsPingRepository,
            TrafficSnapshotRepository trafficSnapshotRepository,
            HistoricalPatternRepository historicalPatternRepository,
            TrafficAnomalyRepository trafficAnomalyRepository,
            IncidentRealtimePublisher incidentRealtimePublisher
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.gpsPingRepository = gpsPingRepository;
        this.trafficSnapshotRepository = trafficSnapshotRepository;
        this.historicalPatternRepository = historicalPatternRepository;
        this.trafficAnomalyRepository = trafficAnomalyRepository;
        this.incidentRealtimePublisher = incidentRealtimePublisher;
    }

    @Override
    public GpsPingReceipt handle(ReceiveGpsPingCommand command) {
        GpsPing ping = new GpsPing(
                UUID.nameUUIDFromBytes(command.deviceId().getBytes()),
                command.latitude(),
                command.longitude(),
                command.timestamp(),
                command.speedKmh()
        );
        gpsPingRepository.save(ping);

        RoadSegment segment = resolveNearestSegment(command.latitude(), command.longitude());
        HistoricalPattern pattern = historicalPatternRepository
                .findBySegmentIdAndDayOfWeekAndHour(
                        segment.id(),
                        command.timestamp().atZone(BAKU_ZONE).getDayOfWeek(),
                        command.timestamp().atZone(BAKU_ZONE).getHour()
                )
                .orElse(null);

        double baselineSpeed = pattern != null
                ? pattern.averageSpeedKmh()
                : trafficSnapshotRepository.findLatestBySegmentId(segment.id())
                .map(TrafficSnapshot::averageSpeedKmh)
                .orElse(35.0);

        int congestionLevel = TrafficMath.congestionFromSpeed(command.speedKmh());
        TrafficSnapshot snapshot = new TrafficSnapshot(segment.id(), command.timestamp(), command.speedKmh(), congestionLevel);
        trafficSnapshotRepository.save(snapshot);

        double stdDev = pattern != null ? pattern.standardDeviation() : 8.0;
        double zScore = TrafficMath.zScore(command.speedKmh(), baselineSpeed, stdDev);
        TrafficAnomaly anomaly = null;
        if (Math.abs(zScore) >= 2.0 || command.speedKmh() < baselineSpeed * 0.55) {
            anomaly = new TrafficAnomaly(
                    segment.id(),
                    command.timestamp(),
                    zScore,
                    AnomalyStatus.ACTIVE,
                    "Traffic speed is significantly below the expected baseline"
            );
            trafficAnomalyRepository.save(anomaly);
            incidentRealtimePublisher.publishCreated(new RoadIncident(
                    UUID.nameUUIDFromBytes((segment.id().toString() + command.timestamp()).getBytes(StandardCharsets.UTF_8)),
                    segment.id(),
                    "STATISTICAL_ANOMALY",
                    "ANOMALY_DETECTION",
                    anomaly.description(),
                    command.timestamp(),
                    true
            ));
        }

        return new GpsPingReceipt(ping, segment, snapshot, anomaly);
    }

    private RoadSegment resolveNearestSegment(double latitude, double longitude) {
        List<RoadSegment> segments = roadSegmentRepository.findAll();
        return segments.stream()
                .min(Comparator.comparingDouble(segment ->
                        TrafficMath.distance(segment.centroid().latitude(), segment.centroid().longitude(), latitude, longitude)))
                .orElseThrow(() -> new IllegalStateException("No road segments are available"));
    }
}
