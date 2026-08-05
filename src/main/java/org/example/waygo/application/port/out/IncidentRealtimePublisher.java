package org.example.waygo.application.port.out;

import org.example.waygo.domain.model.RoadIncident;

public interface IncidentRealtimePublisher {

    void publishCreated(RoadIncident incident);
}
