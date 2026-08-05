package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.out;

import com.waygo.domain.traffic.TrafficAnomaly;

import java.util.List;
import java.util.UUID;

public interface TrafficAnomalyRepository {

    void save(TrafficAnomaly anomaly);

    List<TrafficAnomaly> findActive();

    List<TrafficAnomaly> findBySegmentId(UUID segmentId);
}
