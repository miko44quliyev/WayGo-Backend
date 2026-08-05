package org.example.waygo.application.port.in;

import org.example.waygo.domain.model.RoadIncident;

import java.util.List;

public interface GetIncidentsUseCase {

    List<RoadIncident> handle();
}
