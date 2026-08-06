package com.waygo.traffic.application.service;

import com.waygo.traffic.domain.entity.CityHistoryPoint;
import com.waygo.traffic.domain.entity.CityStats;
import com.waygo.traffic.domain.entity.TrafficSnapshot;


import com.waygo.traffic.application.usecase.GetCityStatsUseCase;
import com.waygo.traffic.application.port.outbound.GpsPingRepository;
import com.waygo.traffic.application.port.outbound.TrafficSnapshotRepository;



import com.waygo.traffic.infrastructure.support.TrafficMath;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GetCityStatsService implements GetCityStatsUseCase {

    private final TrafficSnapshotRepository trafficSnapshotRepository;
    private final GpsPingRepository gpsPingRepository;

    public GetCityStatsService(TrafficSnapshotRepository trafficSnapshotRepository, GpsPingRepository gpsPingRepository) {
        this.trafficSnapshotRepository = trafficSnapshotRepository;
        this.gpsPingRepository = gpsPingRepository;
    }

    @Override
    public CityStats handle() {
        Instant now = Instant.now();
        Instant since = now.minus(24, ChronoUnit.HOURS);

        List<TrafficSnapshot> recentSnapshots = trafficSnapshotRepository.findAll().stream()
                .filter(snapshot -> snapshot.timestamp().isAfter(since))
                .toList();

        double averageSpeed = recentSnapshots.isEmpty()
                ? 0.0
                : recentSnapshots.stream().mapToDouble(TrafficSnapshot::averageSpeedKmh).average().orElse(0.0);

        double congestionPercent = recentSnapshots.isEmpty()
                ? 0.0
                : recentSnapshots.stream().mapToInt(TrafficSnapshot::congestionLevel).average().orElse(0.0);

        long activeVehicles = gpsPingRepository.findAll().stream()
                .filter(ping -> ping.timestamp().isAfter(since))
                .count();

        List<CityHistoryPoint> history = recentSnapshots.stream()
                .collect(Collectors.groupingBy(snapshot -> snapshot.timestamp().truncatedTo(ChronoUnit.HOURS)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new CityHistoryPoint(
                        entry.getKey().atZone(ZoneOffset.UTC).toInstant(),
                        entry.getValue().stream().mapToDouble(TrafficSnapshot::averageSpeedKmh).average().orElse(0.0),
                        (int) Math.round(entry.getValue().stream().mapToInt(TrafficSnapshot::congestionLevel).average().orElse(0.0))
                ))
                .toList();

        return new CityStats(
                averageSpeed,
                TrafficMath.clamp(congestionPercent, 0.0, 100.0),
                activeVehicles,
                history,
                now
        );
    }
}
