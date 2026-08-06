package com.waygo.config.infrastructure.seed;

import com.waygo.traffic.domain.entity.HistoricalPattern;
import com.waygo.traffic.domain.entity.RoadSegment;
import com.waygo.traffic.domain.entity.TrafficAnomaly;
import com.waygo.traffic.domain.entity.TrafficSnapshot;
import com.waygo.traffic.domain.valueobject.AnomalyStatus;
import com.waygo.traffic.domain.valueobject.Coordinate;


import com.waygo.traffic.application.port.outbound.HistoricalPatternRepository;
import com.waygo.traffic.application.port.outbound.RoadSegmentRepository;
import com.waygo.traffic.application.port.outbound.TrafficAnomalyRepository;
import com.waygo.traffic.application.port.outbound.TrafficSnapshotRepository;






import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class SampleDataSeeder {

    private final RoadSegmentRepository roadSegmentRepository;
    private final HistoricalPatternRepository historicalPatternRepository;
    private final TrafficSnapshotRepository trafficSnapshotRepository;
    private final TrafficAnomalyRepository trafficAnomalyRepository;

    public SampleDataSeeder(
            RoadSegmentRepository roadSegmentRepository,
            HistoricalPatternRepository historicalPatternRepository,
            TrafficSnapshotRepository trafficSnapshotRepository,
            TrafficAnomalyRepository trafficAnomalyRepository
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.historicalPatternRepository = historicalPatternRepository;
        this.trafficSnapshotRepository = trafficSnapshotRepository;
        this.trafficAnomalyRepository = trafficAnomalyRepository;
    }

    @PostConstruct
    public void seed() {
        List<RoadSegment> segments = List.of(
                new RoadSegment(
                        UUID.fromString("11111111-1111-1111-1111-111111111111"),
                        "Heydar Aliyev Avenue",
                        List.of(new Coordinate(40.4093, 49.8671), new Coordinate(40.4084, 49.8756)),
                        "Nizami"
                ),
                new RoadSegment(
                        UUID.fromString("22222222-2222-2222-2222-222222222222"),
                        "Koroglu Metro Corridor",
                        List.of(new Coordinate(40.4012, 49.8765), new Coordinate(40.4031, 49.8902)),
                        "Binagadi"
                ),
                new RoadSegment(
                        UUID.fromString("33333333-3333-3333-3333-333333333333"),
                        "Ziya Bunyadov",
                        List.of(new Coordinate(40.4318, 49.8501), new Coordinate(40.4234, 49.8625)),
                        "Yasamal"
                ),
                new RoadSegment(
                        UUID.fromString("44444444-4444-4444-4444-444444444444"),
                        "Nobel Prospect",
                        List.of(new Coordinate(40.3728, 49.8768), new Coordinate(40.3652, 49.8824)),
                        "Khatai"
                ),
                new RoadSegment(
                        UUID.fromString("55555555-5555-5555-5555-555555555555"),
                        "28 May Corridor",
                        List.of(new Coordinate(40.3798, 49.8438), new Coordinate(40.3821, 49.8521)),
                        "Sabayil"
                )
        );
        roadSegmentRepository.saveAll(segments);

        List<HistoricalPattern> patterns = segments.stream()
                .flatMap(segment -> List.of(
                        new HistoricalPattern(segment.id(), DayOfWeek.MONDAY, 8, 22.0, 6.5),
                        new HistoricalPattern(segment.id(), DayOfWeek.MONDAY, 18, 18.0, 7.0),
                        new HistoricalPattern(segment.id(), DayOfWeek.TUESDAY, 8, 23.5, 6.0),
                        new HistoricalPattern(segment.id(), DayOfWeek.WEDNESDAY, 18, 19.5, 5.5),
                        new HistoricalPattern(segment.id(), DayOfWeek.THURSDAY, 9, 24.0, 6.0),
                        new HistoricalPattern(segment.id(), DayOfWeek.FRIDAY, 18, 17.5, 7.2)
                ).stream())
                .toList();
        historicalPatternRepository.saveAll(patterns);

        trafficSnapshotRepository.saveAll(List.of(
                new TrafficSnapshot(segments.get(0).id(), Instant.now().minusSeconds(420), 28.0, 44),
                new TrafficSnapshot(segments.get(1).id(), Instant.now().minusSeconds(360), 24.0, 52),
                new TrafficSnapshot(segments.get(2).id(), Instant.now().minusSeconds(300), 31.0, 38),
                new TrafficSnapshot(segments.get(3).id(), Instant.now().minusSeconds(240), 26.0, 48),
                new TrafficSnapshot(segments.get(4).id(), Instant.now().minusSeconds(180), 34.0, 32)
        ));

        // Seed initial statistical Z-score anomalies for Baku roads
        trafficAnomalyRepository.save(new TrafficAnomaly(
                segments.get(0).id(),
                Instant.now().minusSeconds(300),
                -2.84,
                AnomalyStatus.ACTIVE,
                "Heydar Aliyev Avenue z-score drop"
        ));
        trafficAnomalyRepository.save(new TrafficAnomaly(
                segments.get(1).id(),
                Instant.now().minusSeconds(600),
                -2.31,
                AnomalyStatus.ACTIVE,
                "Koroglu Metro Corridor congestion spike"
        ));
        trafficAnomalyRepository.save(new TrafficAnomaly(
                segments.get(2).id(),
                Instant.now().minusSeconds(900),
                -2.15,
                AnomalyStatus.ACTIVE,
                "Ziya Bunyadov slow traffic flow"
        ));
    }
}
