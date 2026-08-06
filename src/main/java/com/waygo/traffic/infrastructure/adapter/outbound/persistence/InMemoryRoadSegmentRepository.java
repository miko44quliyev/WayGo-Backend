package com.waygo.traffic.infrastructure.adapter.outbound.persistence;

import com.waygo.traffic.domain.entity.RoadSegment;


import com.waygo.traffic.application.port.outbound.RoadSegmentRepository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRoadSegmentRepository implements RoadSegmentRepository {

    private final ConcurrentHashMap<UUID, RoadSegment> storage = new ConcurrentHashMap<>();

    @Override
    public List<RoadSegment> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<RoadSegment> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void saveAll(Collection<RoadSegment> segments) {
        for (RoadSegment segment : segments) {
            storage.put(segment.id(), segment);
        }
    }
}
