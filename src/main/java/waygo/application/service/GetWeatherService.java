package waygo.application.service;

import waygo.application.port.in.GetWeatherUseCase;
import waygo.application.port.in.WeatherQuery;
import waygo.application.port.out.WeatherGateway;
import waygo.domain.model.WeatherSnapshot;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClientException;

@Service
public class GetWeatherService implements GetWeatherUseCase {

    private final WeatherGateway weatherGateway;

    public GetWeatherService(WeatherGateway weatherGateway) {
        this.weatherGateway = weatherGateway;
    }

    @Override
    public WeatherSnapshot handle(WeatherQuery query) {
        try {
            return weatherGateway.fetch(query.locationName(), query.latitude(), query.longitude());
        } catch (RestClientException ex) {
            return weatherGateway.fallback(query.locationName(), query.latitude(), query.longitude(), ex.getMessage());
        }
    }
}
