package com.waygo.config.infrastructure.realtime;

import com.waygo.traffic.domain.entity.IncidentEvent;
import com.waygo.traffic.domain.entity.RoadIncident;


import com.corundumstudio.socketio.SocketIOServer;
import com.waygo.traffic.application.port.outbound.IncidentRealtimePublisher;


import org.springframework.stereotype.Component;

@Component
public class SocketIoIncidentPublisher implements IncidentRealtimePublisher {


import com.corundumstudio.socketio.SocketIOServer;
import com.waygo.traffic.application.port.outbound.IncidentRealtimePublisher;


import org.springframework.stereotype.Component;

@Component
public class SocketIoIncidentPublisher implements IncidentRealtimePublisher {

    private final SocketIOServer socketIOServer;

    public SocketIoIncidentPublisher(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @Override
    public void publishCreated(RoadIncident incident) {
        socketIOServer.getBroadcastOperations().sendEvent("incident:created", IncidentEvent.from(incident));
    }

    @Override
    public void publishReportPending(com.waygo.report.domain.entity.UserReport report) {
        socketIOServer.getBroadcastOperations().sendEvent("report:pending", report);
    }
}
