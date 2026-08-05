package waygo.application.port.out;

import waygo.domain.model.TrafficAnomaly;

import java.util.List;
import java.util.UUID;

public interface TrafficAnomalyRepository {

    void save(TrafficAnomaly anomaly);

    List<TrafficAnomaly> findActive();

    List<TrafficAnomaly> findBySegmentId(UUID segmentId);
}
