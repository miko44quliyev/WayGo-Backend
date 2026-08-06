package com.waygo.traffic.application.usecase;

import com.waygo.traffic.domain.entity.RoadIncident;




import java.util.List;

public interface GetIncidentsUseCase {

    List<RoadIncident> handle();
}
