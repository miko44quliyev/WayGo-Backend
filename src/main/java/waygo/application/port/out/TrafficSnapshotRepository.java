package waygo.application.port.out;

import waygo.domain.model.TrafficSnapshot;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrafficSnapshotRepository {

    void save(TrafficSnapshot snapshot);

    Optional<TrafficSnapshot> findLatestBySegmentId(UUID segmentId);

    List<TrafficSnapshot> findLatestAll();

    List<TrafficSnapshot> findAll();

    void saveAll(Collection<TrafficSnapshot> snapshots);
}
