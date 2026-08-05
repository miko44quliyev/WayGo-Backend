package waygo.application.port.out;

import waygo.domain.model.RoadIncident;

public interface IncidentRealtimePublisher {

    void publishCreated(RoadIncident incident);
}
