package com.waygo.application.service;

import com.waygo.domain.model.*;

import com.waygo.application.port.out.TrafficPredictionEngine;
import com.waygo.domain.model.AnomalyStatus;
import com.waygo.domain.model.TrafficAnomaly;
import com.waygo.domain.model.Coordinate;
import com.waygo.domain.model.HistoricalPattern;
import com.waygo.domain.model.RoadSegment;
import com.waygo.domain.model.TrafficForecast;
import com.waygo.domain.model.TrafficMapView;
import com.waygo.domain.model.TrafficSnapshot;
import com.waygo.infrastructure.persistence.repository.InMemoryHistoricalPatternRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryRoadSegmentRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryTrafficAnomalyRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryTrafficSnapshotRepository;
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
        anomalies.save(new TrafficAnomaly(
                segmentId,
                Instant.parse("2026-08-05T00:00:00Z"),
                3.0,
                AnomalyStatus.ACTIVE,
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
