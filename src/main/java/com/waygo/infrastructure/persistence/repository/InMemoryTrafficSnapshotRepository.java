package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.infrastructure.persistence.repository;

import com.waygo.application.port.out.TrafficSnapshotRepository;
import com.waygo.domain.traffic.TrafficSnapshot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTrafficSnapshotRepository implements TrafficSnapshotRepository {

    private final ConcurrentHashMap<UUID, List<TrafficSnapshot>> storage = new ConcurrentHashMap<>();

    @Override
    public void save(TrafficSnapshot snapshot) {
        storage.compute(snapshot.segmentId(), (segmentId, snapshots) -> {
            List<TrafficSnapshot> values = snapshots == null ? new ArrayList<>() : new ArrayList<>(snapshots);
            values.add(snapshot);
            values.sort(Comparator.comparing(TrafficSnapshot::timestamp));
            return values;
        });
    }

    @Override
    public Optional<TrafficSnapshot> findLatestBySegmentId(UUID segmentId) {
        List<TrafficSnapshot> snapshots = storage.get(segmentId);
        if (snapshots == null || snapshots.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(snapshots.get(snapshots.size() - 1));
    }

    @Override
    public List<TrafficSnapshot> findLatestAll() {
        List<TrafficSnapshot> result = new ArrayList<>();
        for (List<TrafficSnapshot> snapshots : storage.values()) {
            if (!snapshots.isEmpty()) {
                result.add(snapshots.get(snapshots.size() - 1));
            }
        }
        return result;
    }

    @Override
    public List<TrafficSnapshot> findAll() {
        List<TrafficSnapshot> result = new ArrayList<>();
        for (List<TrafficSnapshot> snapshots : storage.values()) {
            result.addAll(snapshots);
        }
        return result;
    }

    @Override
    public void saveAll(Collection<TrafficSnapshot> snapshots) {
        for (TrafficSnapshot snapshot : snapshots) {
            save(snapshot);
        }
    }
}
