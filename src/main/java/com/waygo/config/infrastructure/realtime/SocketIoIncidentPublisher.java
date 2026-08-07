package com.waygo.config.infrastructure.realtime;

import com.corundumstudio.socketio.SocketIOServer;
import com.waygo.traffic.application.port.outbound.IncidentRealtimePublisher;
import com.waygo.traffic.domain.entity.IncidentEvent;
import com.waygo.traffic.domain.entity.RoadIncident;
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
        java.util.Map<String, Object> dto = new java.util.HashMap<>();
        dto.put("id", report.id());
        dto.put("userId", report.userId());
        dto.put("segmentId", report.segmentId());
        dto.put("type", report.type().name());
        dto.put("description", report.description());
        dto.put("createdAt", report.createdAt().toString()); // Fixes Jackson Instant error
        dto.put("status", report.status().name());
        dto.put("latitude", report.latitude());
        dto.put("longitude", report.longitude());
        socketIOServer.getBroadcastOperations().sendEvent("report:pending", dto);
    }
}
