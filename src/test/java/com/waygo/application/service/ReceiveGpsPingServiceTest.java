package com.waygo.application.service;

import com.waygo.domain.model.*;

import com.waygo.application.port.out.IncidentRealtimePublisher;
import com.waygo.application.port.in.ReceiveGpsPingCommand;
import com.waygo.domain.model.Coordinate;
import com.waygo.domain.model.HistoricalPattern;
import com.waygo.domain.model.RoadSegment;
import com.waygo.infrastructure.persistence.repository.InMemoryGpsPingRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryHistoricalPatternRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryRoadSegmentRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryTrafficAnomalyRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryTrafficSnapshotRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ReceiveGpsPingServiceTest {

    @Test
    void handleShouldPersistPingSnapshotAndAnomalyWhenSpeedDropsBelowBaseline() {
        InMemoryRoadSegmentRepository roadSegments = new InMemoryRoadSegmentRepository();
        InMemoryGpsPingRepository gpsPings = new InMemoryGpsPingRepository();
        InMemoryTrafficSnapshotRepository snapshots = new InMemoryTrafficSnapshotRepository();
        InMemoryHistoricalPatternRepository patterns = new InMemoryHistoricalPatternRepository();
        InMemoryTrafficAnomalyRepository anomalies = new InMemoryTrafficAnomalyRepository();

        UUID segmentId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        RoadSegment segment = new RoadSegment(
                segmentId,
                "Test Segment",
                List.of(new Coordinate(40.0, 49.0), new Coordinate(40.01, 49.02)),
                "Test Zone"
        );
        roadSegments.saveAll(List.of(segment));
        patterns.saveAll(List.of(new HistoricalPattern(segmentId, DayOfWeek.MONDAY, 8, 50.0, 5.0)));

        ReceiveGpsPingService service = new ReceiveGpsPingService(
                roadSegments,
                gpsPings,
                snapshots,
                patterns,
                anomalies,
                mock(IncidentRealtimePublisher.class)
        );

        var receipt = service.handle(new ReceiveGpsPingCommand(
                "device-1",
                40.0,
                49.0,
                Instant.parse("2026-08-05T05:00:00Z"),
                10.0
        ));

        assertEquals(segmentId, receipt.segment().id());
        assertNotNull(receipt.snapshot());
        assertNotNull(receipt.anomaly());
        assertEquals(1, gpsPings.findAll().size());
        assertEquals(1, snapshots.findAll().size());
        assertEquals(1, anomalies.findActive().size());
        assertTrue(receipt.anomaly().description().contains("baseline"));
    }
}
