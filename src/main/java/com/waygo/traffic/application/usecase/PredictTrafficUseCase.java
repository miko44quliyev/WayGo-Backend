package com.waygo.traffic.application.usecase;

import com.waygo.traffic.domain.entity.TrafficForecast;


import com.waygo.traffic.application.dto.PredictTrafficQuery;
import com.waygo.traffic.application.usecase.PredictTrafficUseCase;
import com.waygo.traffic.application.port.outbound.HistoricalPatternRepository;
import com.waygo.traffic.application.port.outbound.RoadSegmentRepository;
import com.waygo.traffic.application.port.outbound.TrafficPredictionEngine;
import com.waygo.traffic.application.port.outbound.TrafficSnapshotRepository;

import org.springframework.stereotype.Service;

@Service
public class PredictTrafficUseCase {

    private final RoadSegmentRepository roadSegmentRepository;
    private final TrafficSnapshotRepository trafficSnapshotRepository;
    private final HistoricalPatternRepository historicalPatternRepository;
    private final TrafficPredictionEngine trafficPredictionEngine;

    public PredictTrafficUseCase(
            RoadSegmentRepository roadSegmentRepository,
            TrafficSnapshotRepository trafficSnapshotRepository,
            HistoricalPatternRepository historicalPatternRepository,
            TrafficPredictionEngine trafficPredictionEngine
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.trafficSnapshotRepository = trafficSnapshotRepository;
        this.historicalPatternRepository = historicalPatternRepository;
        this.trafficPredictionEngine = trafficPredictionEngine;
    }

    public TrafficForecast handle(PredictTrafficQuery query) {
        var segment = roadSegmentRepository.findById(query.segmentId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown road segment: " + query.segmentId()));
        var latestSnapshot = trafficSnapshotRepository.findLatestBySegmentId(segment.id());
        var pattern = historicalPatternRepository.findBySegmentIdAndDayOfWeekAndHour(segment.id(), query.dayOfWeek(), query.hour());
        int sampleCount = historicalPatternRepository.findBySegmentId(segment.id()).size();
        return trafficPredictionEngine.forecast(segment, latestSnapshot, pattern, query.dayOfWeek(), query.hour(), sampleCount);
    }
}


