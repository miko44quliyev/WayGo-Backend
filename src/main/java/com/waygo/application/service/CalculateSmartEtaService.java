package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.service;

import com.waygo.application.port.in.CalculateSmartEtaUseCase;
import com.waygo.application.port.out.HistoricalPatternRepository;
import com.waygo.application.port.out.RoadSegmentRepository;
import com.waygo.application.port.out.TrafficPredictionEngine;
import com.waygo.application.port.out.TrafficSnapshotRepository;
import com.waygo.domain.traffic.EtaWindow;
import com.waygo.domain.traffic.RoadSegment;
import com.waygo.domain.traffic.SmartEtaResult;
import com.waygo.domain.traffic.TrafficForecast;
import com.waygo.domain.traffic.TrafficSnapshot;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CalculateSmartEtaService implements CalculateSmartEtaUseCase {

    private static final ZoneId BAKU_ZONE = ZoneId.of("Asia/Baku");

    private final RoadSegmentRepository roadSegmentRepository;
    private final TrafficSnapshotRepository trafficSnapshotRepository;
    private final HistoricalPatternRepository historicalPatternRepository;
    private final TrafficPredictionEngine trafficPredictionEngine;

    public CalculateSmartEtaService(
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
    public SmartEtaResult handle(List<UUID> segmentIds) {
        if (segmentIds == null || segmentIds.isEmpty()) {
            throw new IllegalArgumentException("segmentIds must not be empty");
        }

        Instant now = Instant.now();
        double baselineMinutes = estimateRouteMinutes(segmentIds, now);
        List<EtaWindow> windows = List.of(
                buildWindow(segmentIds, now, 0),
                buildWindow(segmentIds, now, 15),
                buildWindow(segmentIds, now, 30),
                buildWindow(segmentIds, now, 60)
        );
        return new SmartEtaResult(windows, baselineMinutes, "Route built from the provided ordered road segments");
    }

    private EtaWindow buildWindow(List<UUID> segmentIds, Instant baseTime, int offsetMinutes) {
        Instant departureAt = baseTime.plusSeconds(offsetMinutes * 60L);
        double travelMinutes = estimateRouteMinutes(segmentIds, departureAt);
        Instant arrivalAt = departureAt.plusSeconds(Math.round(travelMinutes * 60.0));
        List<String> breakdown = new ArrayList<>();
        for (UUID segmentId : segmentIds) {
            RoadSegment segment = roadSegmentRepository.findById(segmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown road segment: " + segmentId));
            TrafficForecast forecast = predictFor(segment, departureAt);
            breakdown.add(segment.name() + "=" + Math.round(forecast.predictedSpeedKmh()) + "km/h");
        }
        return new EtaWindow(offsetMinutes, departureAt, travelMinutes, arrivalAt, breakdown);
    }

    private double estimateRouteMinutes(List<UUID> segmentIds, Instant atTime) {
        double totalMinutes = 0.0;
        DayOfWeek dayOfWeek = atTime.atZone(BAKU_ZONE).getDayOfWeek();
        int hour = atTime.atZone(BAKU_ZONE).getHour();
        for (UUID segmentId : segmentIds) {
            RoadSegment segment = roadSegmentRepository.findById(segmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown road segment: " + segmentId));
            TrafficForecast forecast = predictFor(segment, dayOfWeek, hour);
            double segmentLengthKm = Math.max(0.4, approximateLengthKm(segment));
            totalMinutes += segmentLengthKm / Math.max(5.0, forecast.predictedSpeedKmh()) * 60.0;
        }
        return totalMinutes;
    }

    private TrafficForecast predictFor(RoadSegment segment, Instant atTime) {
        DayOfWeek dayOfWeek = atTime.atZone(BAKU_ZONE).getDayOfWeek();
        int hour = atTime.atZone(BAKU_ZONE).getHour();
        return predictFor(segment, dayOfWeek, hour);
    }

    private TrafficForecast predictFor(RoadSegment segment, DayOfWeek dayOfWeek, int hour) {
        Optional<TrafficSnapshot> latestSnapshot = trafficSnapshotRepository.findLatestBySegmentId(segment.id());
        var pattern = historicalPatternRepository.findBySegmentIdAndDayOfWeekAndHour(segment.id(), dayOfWeek, hour);
        int sampleCount = historicalPatternRepository.findBySegmentId(segment.id()).size();
        return trafficPredictionEngine.forecast(segment, latestSnapshot, pattern, dayOfWeek, hour, sampleCount);
    }

    private double approximateLengthKm(RoadSegment segment) {
        if (segment.coordinates().size() < 2) {
            return 1.0;
        }
        double sum = 0.0;
        for (int i = 1; i < segment.coordinates().size(); i++) {
            var prev = segment.coordinates().get(i - 1);
            var next = segment.coordinates().get(i);
            double degrees = Math.sqrt(Math.pow(prev.latitude() - next.latitude(), 2) + Math.pow(prev.longitude() - next.longitude(), 2));
            sum += degrees * 111.0;
        }
        return sum;
    }
}
