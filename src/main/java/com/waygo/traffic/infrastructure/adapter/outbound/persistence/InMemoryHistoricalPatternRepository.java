package com.waygo.traffic.infrastructure.adapter.outbound.persistence;

import com.waygo.traffic.domain.entity.HistoricalPattern;


import com.waygo.traffic.application.port.outbound.HistoricalPatternRepository;

import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryHistoricalPatternRepository implements HistoricalPatternRepository {

    private final ConcurrentHashMap<UUID, List<HistoricalPattern>> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<HistoricalPattern> findBySegmentIdAndDayOfWeekAndHour(UUID segmentId, DayOfWeek dayOfWeek, int hour) {
        return storage.getOrDefault(segmentId, List.of()).stream()
                .filter(pattern -> pattern.dayOfWeek() == dayOfWeek && pattern.hour() == hour)
                .findFirst();
    }

    @Override
    public List<HistoricalPattern> findBySegmentId(UUID segmentId) {
        return new ArrayList<>(storage.getOrDefault(segmentId, List.of()));
    }

    @Override
    public void saveAll(Collection<HistoricalPattern> patterns) {
        for (HistoricalPattern pattern : patterns) {
            storage.compute(pattern.segmentId(), (segmentId, existing) -> {
                List<HistoricalPattern> values = existing == null ? new ArrayList<>() : new ArrayList<>(existing);
                values.add(pattern);
                return values;
            });
        }
    }
}
