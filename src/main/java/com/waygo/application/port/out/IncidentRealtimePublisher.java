package com.waygo.application.port.out;

import com.waygo.domain.model.*;



public interface IncidentRealtimePublisher {

    void publishCreated(RoadIncident incident);
}
