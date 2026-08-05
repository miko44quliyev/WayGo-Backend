package com.waygo.application.port.out;

import com.waygo.domain.traffic.RoadIncident;

public interface IncidentRealtimePublisher {

    void publishCreated(RoadIncident incident);
}
