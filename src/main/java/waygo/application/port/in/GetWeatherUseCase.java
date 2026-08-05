package waygo.application.port.in;

import waygo.domain.model.WeatherSnapshot;

public interface GetWeatherUseCase {

    WeatherSnapshot handle(WeatherQuery query);
}
