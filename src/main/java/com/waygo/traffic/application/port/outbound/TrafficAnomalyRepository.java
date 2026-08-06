package com.waygo.traffic.application.port.outbound;

import com.waygo.traffic.domain.entity.TrafficAnomaly;




import java.util.List;
import java.util.UUID;

public interface TrafficAnomalyRepository {

    void save(TrafficAnomaly anomaly);

    List<TrafficAnomaly> findActive();

    List<TrafficAnomaly> findBySegmentId(UUID segmentId);
}
