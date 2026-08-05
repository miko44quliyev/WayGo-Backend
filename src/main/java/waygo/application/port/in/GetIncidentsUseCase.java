package waygo.application.port.in;

import waygo.domain.model.RoadIncident;

import java.util.List;

public interface GetIncidentsUseCase {

    List<RoadIncident> handle();
}
