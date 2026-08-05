package waygo.application.port.out;

import waygo.domain.model.WeatherSnapshot;

public interface WeatherGateway {

    WeatherSnapshot fetch(String locationName, double latitude, double longitude);

    WeatherSnapshot fallback(String locationName, double latitude, double longitude, String reason);
}
