package com.waygo.traffic.application.usecase;

import com.waygo.traffic.domain.entity.TrafficAnomaly;


import com.waygo.traffic.application.usecase.GetAnomaliesUseCase;
import com.waygo.traffic.application.port.outbound.TrafficAnomalyRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GetAnomaliesUseCase {

    private final TrafficAnomalyRepository trafficAnomalyRepository;

    public GetAnomaliesUseCase(TrafficAnomalyRepository trafficAnomalyRepository) {
        this.trafficAnomalyRepository = trafficAnomalyRepository;
    }

    public List<TrafficAnomaly> handle() {
        return trafficAnomalyRepository.findActive().stream()
                .sorted(Comparator.comparing(TrafficAnomaly::detectedAt).reversed())
                .toList();
    }
}


