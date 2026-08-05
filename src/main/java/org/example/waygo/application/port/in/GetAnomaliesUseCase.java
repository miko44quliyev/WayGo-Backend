package org.example.waygo.application.port.in;

import org.example.waygo.domain.model.TrafficAnomaly;

import java.util.List;

public interface GetAnomaliesUseCase {

    List<TrafficAnomaly> handle();
}
