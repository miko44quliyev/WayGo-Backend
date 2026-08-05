package com.waygo.application.service;

import com.waygo.domain.model.*;

import com.waygo.application.port.out.HistoricalPatternRepository;
import com.waygo.application.port.out.RoadSegmentRepository;
import com.waygo.application.port.out.TrafficPredictionEngine;
import com.waygo.application.port.out.TrafficSnapshotRepository;
import com.waygo.domain.model.Coordinate;
import com.waygo.domain.model.HistoricalPattern;
import com.waygo.domain.model.RoadSegment;
import com.waygo.domain.model.SmartEtaResult;
import com.waygo.domain.model.TrafficForecast;
import com.waygo.domain.model.TrafficSnapshot;
import com.waygo.infrastructure.persistence.repository.InMemoryHistoricalPatternRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryRoadSegmentRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryTrafficSnapshotRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalculateSmartEtaServiceTest {

    @Test
    void handleShouldReturnFourEtaWindows() {
        RoadSegmentRepository roadSegments = new InMemoryRoadSegmentRepository();
        TrafficSnapshotRepository snapshots = new InMemoryTrafficSnapshotRepository();
        HistoricalPatternRepository patterns = new InMemoryHistoricalPatternRepository();
        TrafficPredictionEngine engine = mock(TrafficPredictionEngine.class);

        UUID segment1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID segment2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
        roadSegments.saveAll(List.of(
                new RoadSegment(segment1, "Segment 1", List.of(new Coordinate(40.0, 49.0), new Coordinate(40.01, 49.02)), "Zone A"),
                new RoadSegment(segment2, "Segment 2", List.of(new Coordinate(40.02, 49.03), new Coordinate(40.04, 49.05)), "Zone B")
        ));
        snapshots.save(new TrafficSnapshot(segment1, Instant.parse("2026-08-05T00:00:00Z"), 30.0, 40));
        snapshots.save(new TrafficSnapshot(segment2, Instant.parse("2026-08-05T00:00:00Z"), 25.0, 50));
        patterns.saveAll(List.of(
                new HistoricalPattern(segment1, DayOfWeek.MONDAY, 8, 30.0, 5.0),
                new HistoricalPattern(segment2, DayOfWeek.MONDAY, 8, 25.0, 5.0)
        ));

        when(engine.forecast(any(), any(Optional.class), any(Optional.class), any(), anyInt(), anyInt())).thenAnswer(invocation -> {
            RoadSegment segment = invocation.getArgument(0);
            double speed = segment.id().equals(segment1) ? 30.0 : 25.0;
            return new TrafficForecast(segment.id(), segment.name(), DayOfWeek.MONDAY, 8, speed, 100 - (int) Math.round(speed * 2), 0.8, "ok");
        });

        CalculateSmartEtaService service = new CalculateSmartEtaService(roadSegments, snapshots, patterns, engine);
        SmartEtaResult result = service.handle(List.of(segment1, segment2));

        assertEquals(4, result.windows().size());
        assertTrue(result.baselineTravelMinutes() > 0.0);
        assertFalse(result.windows().get(0).segmentBreakdown().isEmpty());
        assertEquals(0, result.windows().get(0).departureOffsetMinutes());
    }
}
