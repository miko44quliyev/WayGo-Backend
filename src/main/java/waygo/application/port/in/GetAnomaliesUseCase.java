package waygo.application.port.in;

import waygo.domain.model.TrafficAnomaly;

import java.util.List;

public interface GetAnomaliesUseCase {

    List<TrafficAnomaly> handle();
}
