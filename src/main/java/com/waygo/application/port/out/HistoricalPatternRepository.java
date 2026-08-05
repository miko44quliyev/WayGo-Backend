package com.waygo.application.port.out;

import com.waygo.domain.traffic.HistoricalPattern;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoricalPatternRepository {

    Optional<HistoricalPattern> findBySegmentIdAndDayOfWeekAndHour(UUID segmentId, DayOfWeek dayOfWeek, int hour);

    List<HistoricalPattern> findBySegmentId(UUID segmentId);

    void saveAll(Collection<HistoricalPattern> patterns);
}
