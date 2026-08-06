package com.waygo.traffic.infrastructure.adapter.outbound.persistence;

import com.waygo.traffic.domain.entity.TrafficAnomaly;
import com.waygo.traffic.domain.valueobject.AnomalyStatus;


import com.waygo.traffic.application.port.outbound.TrafficAnomalyRepository;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryTrafficAnomalyRepository implements TrafficAnomalyRepository {

    private final CopyOnWriteArrayList<TrafficAnomaly> storage = new CopyOnWriteArrayList<>();

    @Override
    public void save(TrafficAnomaly anomaly) {
        storage.add(anomaly);
    }

    @Override
    public List<TrafficAnomaly> findActive() {
        return storage.stream()
                .filter(anomaly -> anomaly.status() == AnomalyStatus.ACTIVE)
                .toList();
    }

    @Override
    public List<TrafficAnomaly> findBySegmentId(UUID segmentId) {
        return storage.stream()
                .filter(anomaly -> anomaly.segmentId().equals(segmentId))
                .toList();
    }
}
