package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.service;

import com.waygo.application.port.in.GetWeatherUseCase;
import com.waygo.application.port.in.WeatherQuery;
import com.waygo.application.port.out.WeatherGateway;
import com.waygo.domain.traffic.WeatherSnapshot;
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
