package com.waygo.traffic.application.dto;

import com.waygo.traffic.domain.entity.RoadIncident;




import java.util.List;

public interface GetIncidentsUseCase {

    List<RoadIncident> handle();
}
