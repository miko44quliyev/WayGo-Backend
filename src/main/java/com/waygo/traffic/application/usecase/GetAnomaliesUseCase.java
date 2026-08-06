package com.waygo.traffic.application.usecase;

import com.waygo.traffic.domain.entity.TrafficAnomaly;




import java.util.List;

public interface GetAnomaliesUseCase {

    List<TrafficAnomaly> handle();
}
