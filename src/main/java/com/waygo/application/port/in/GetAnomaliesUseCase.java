package com.waygo.application.port.in;

import com.waygo.domain.model.*;



import java.util.List;

public interface GetAnomaliesUseCase {

    List<TrafficAnomaly> handle();
}
