package waygo.application.service;

import waygo.infrastructure.persistence.InMemoryGpsPingRepository;
import waygo.infrastructure.persistence.InMemoryTrafficSnapshotRepository;
import waygo.domain.model.GpsPing;
import waygo.domain.model.TrafficSnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetCityStatsServiceTest {

    @Test
    void handleShouldAggregateRecentSnapshotsAndGpsPings() {
        InMemoryTrafficSnapshotRepository snapshots = new InMemoryTrafficSnapshotRepository();
        InMemoryGpsPingRepository gpsPings = new InMemoryGpsPingRepository();
        Instant now = Instant.parse("2026-08-05T00:00:00Z");

        snapshots.save(new TrafficSnapshot(UUID.randomUUID(), now.minusSeconds(3600), 20.0, 20));
        snapshots.save(new TrafficSnapshot(UUID.randomUUID(), now.minusSeconds(7200), 30.0, 30));
        snapshots.save(new TrafficSnapshot(UUID.randomUUID(), now.minusSeconds(90000), 90.0, 90));
        gpsPings.save(new GpsPing(UUID.randomUUID(), 40.0, 49.0, now.minusSeconds(1800), 25.0));
        gpsPings.save(new GpsPing(UUID.randomUUID(), 40.0, 49.0, now.minusSeconds(90000), 25.0));

        GetCityStatsService service = new GetCityStatsService(snapshots, gpsPings);
        var stats = service.handle();

        assertTrue(stats.averageSpeedKmh() > 0.0);
        assertTrue(stats.congestionPercent() > 0.0);
        assertEquals(1, stats.activeVehiclesCount());
        assertEquals(2, stats.last24Hours().size());
    }
}
