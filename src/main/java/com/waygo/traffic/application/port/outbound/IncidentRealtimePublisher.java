package com.waygo.traffic.application.port.outbound;

import com.waygo.traffic.domain.entity.RoadIncident;




public interface IncidentRealtimePublisher {

    void publishCreated(RoadIncident incident);
}
