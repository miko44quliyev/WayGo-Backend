package waygo.application.port.in;

import waygo.domain.model.TrafficForecast;

public interface PredictTrafficUseCase {

    TrafficForecast handle(PredictTrafficQuery query);
}
