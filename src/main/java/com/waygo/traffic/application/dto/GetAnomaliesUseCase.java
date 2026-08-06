package com.waygo.traffic.application.dto;

import com.waygo.traffic.domain.entity.TrafficAnomaly;




import java.util.List;

public interface GetAnomaliesUseCase {

    List<TrafficAnomaly> handle();
}
