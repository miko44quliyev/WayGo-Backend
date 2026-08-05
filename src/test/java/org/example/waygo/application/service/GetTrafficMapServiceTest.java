package org.example.waygo.application.service;

import org.example.waygo.application.port.out.HistoricalPatternRepository;
import org.example.waygo.application.port.out.RoadSegmentRepository;
import org.example.waygo.application.port.out.TrafficAnomalyRepository;
import org.example.waygo.application.port.out.TrafficPredictionEngine;
import org.example.waygo.application.port.out.TrafficSnapshotRepository;
import org.example.waygo.domain.model.Coordinate;
import org.example.waygo.domain.model.HistoricalPattern;
import org.example.waygo.domain.model.RoadSegment;
import org.example.waygo.domain.model.TrafficForecast;
import org.example.waygo.domain.model.TrafficMapView;
import org.example.waygo.domain.model.TrafficSnapshot;
import org.example.waygo.infrastructure.persistence.InMemoryHistoricalPatternRepository;
import org.example.waygo.infrastructure.persistence.InMemoryRoadSegmentRepository;
import org.example.waygo.infrastructure.persistence.InMemoryTrafficAnomalyRepository;
import org.example.waygo.infrastructure.persistence.InMemoryTrafficSnapshotRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetTrafficMapServiceTest {

    @Test
    void handleShouldReturnSegmentEntriesWithAnomalyFlag() {
        InMemoryRoadSegmentRepository roadSegments = new InMemoryRoadSegmentRepository();
        InMemoryTrafficSnapshotRepository snapshots = new InMemoryTrafficSnapshotRepository();
        InMemoryHistoricalPatternRepository patterns = new InMemoryHistoricalPatternRepository();
        InMemoryTrafficAnomalyRepository anomalies = new InMemoryTrafficAnomalyRepository();
        TrafficPredictionEngine engine = mock(TrafficPredictionEngine.class);

        UUID segmentId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        RoadSegment segment = new RoadSegment(
                segmentId,
                "Test Segment",
                List.of(new Coordinate(40.0, 49.0), new Coordinate(40.01, 49.02)),
                "Test Zone"
        );
        roadSegments.saveAll(List.of(segment));
        snapshots.save(new TrafficSnapshot(segmentId, Instant.parse("2026-08-05T00:00:00Z"), 28.0, 44));
        patterns.saveAll(List.of(new HistoricalPattern(segmentId, DayOfWeek.MONDAY, 8, 30.0, 5.0)));
        anomalies.save(new org.example.waygo.domain.model.TrafficAnomaly(
                segmentId,
                Instant.parse("2026-08-05T00:00:00Z"),
                3.0,
                org.example.waygo.domain.model.AnomalyStatus.ACTIVE,
                "test anomaly"
        ));

        when(engine.forecast(any(), any(Optional.class), any(Optional.class), any(), anyInt(), anyInt())).thenReturn(
                new TrafficForecast(segmentId, "Test Segment", DayOfWeek.MONDAY, 8, 26.0, 48, 0.82, "ok")
        );

        GetTrafficMapService service = new GetTrafficMapService(roadSegments, snapshots, patterns, anomalies, engine);
        TrafficMapView view = service.handle();

        assertEquals(1, view.segments().size());
        assertEquals(segmentId, view.segments().get(0).segmentId());
        assertTrue(view.segments().get(0).anomalyDetected());
        assertEquals(26.0, view.segments().get(0).predictedSpeedKmh());
    }
}
