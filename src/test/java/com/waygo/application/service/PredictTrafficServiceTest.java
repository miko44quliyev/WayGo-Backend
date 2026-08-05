package com.waygo.application.service;

import com.waygo.domain.model.*;

import com.waygo.application.port.in.PredictTrafficQuery;
import com.waygo.application.port.out.TrafficPredictionEngine;
import com.waygo.domain.model.Coordinate;
import com.waygo.domain.model.HistoricalPattern;
import com.waygo.domain.model.RoadSegment;
import com.waygo.domain.model.TrafficForecast;
import com.waygo.domain.model.TrafficSnapshot;
import com.waygo.infrastructure.persistence.repository.InMemoryHistoricalPatternRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryRoadSegmentRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryTrafficSnapshotRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PredictTrafficServiceTest {

    @Test
    void handleShouldDelegateToPredictionEngineWithKnownSegment() {
        InMemoryRoadSegmentRepository roadSegments = new InMemoryRoadSegmentRepository();
        InMemoryTrafficSnapshotRepository snapshots = new InMemoryTrafficSnapshotRepository();
        InMemoryHistoricalPatternRepository patterns = new InMemoryHistoricalPatternRepository();
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

        when(engine.forecast(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(
                new TrafficForecast(segmentId, "Test Segment", DayOfWeek.MONDAY, 8, 26.0, 48, 0.82, "ok")
        );

        PredictTrafficService service = new PredictTrafficService(roadSegments, snapshots, patterns, engine);
        TrafficForecast forecast = service.handle(new PredictTrafficQuery(segmentId, DayOfWeek.MONDAY, 8));

        assertEquals(26.0, forecast.predictedSpeedKmh());
        assertEquals(48, forecast.predictedCongestionLevel());
    }
}
