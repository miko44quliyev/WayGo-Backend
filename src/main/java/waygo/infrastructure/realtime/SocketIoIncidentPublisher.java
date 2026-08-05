package waygo.infrastructure.realtime;

import com.corundumstudio.socketio.SocketIOServer;
import waygo.application.port.out.IncidentRealtimePublisher;
import waygo.domain.model.IncidentEvent;
import waygo.domain.model.RoadIncident;
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
}
