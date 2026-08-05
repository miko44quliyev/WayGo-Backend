package org.example.waygo.infrastructure.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.waygo.application.port.out.WeatherGateway;
import org.example.waygo.domain.model.WeatherSnapshot;
import org.example.waygo.infrastructure.support.TrafficMath;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenMeteoWeatherGateway implements WeatherGateway {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenMeteoWeatherGateway(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    @Override
    public WeatherSnapshot fetch(String locationName, double latitude, double longitude) {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                + "&longitude=" + longitude
                + "&current=temperature_2m,precipitation,wind_speed_10m,weather_code"
                + "&timezone=auto";
        try {
            String body = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(body);
            JsonNode current = root.path("current");
            double temperature = current.path("temperature_2m").asDouble(0.0);
            double windSpeed = current.path("wind_speed_10m").asDouble(0.0);
            double precipitation = current.path("precipitation").asDouble(0.0);
            int impact = calculateImpact(temperature, windSpeed, precipitation);
            return new WeatherSnapshot(
                    locationName,
                    latitude,
                    longitude,
                    temperature,
                    windSpeed,
                    precipitation,
                    weatherDescription(current.path("weather_code").asInt(-1)),
                    impact,
                    "open-meteo"
            );
        } catch (Exception ex) {
            if (ex instanceof RestClientException) {
                throw (RestClientException) ex;
            }
            throw new RestClientException("Failed to parse Open-Meteo response", ex);
        }
    }

    @Override
    public WeatherSnapshot fallback(String locationName, double latitude, double longitude, String reason) {
        double temperature = 18.0 + ((Math.abs(latitude) + Math.abs(longitude)) % 7.0);
        double windSpeed = 10.0 + ((Math.abs(latitude - longitude) * 3.0) % 20.0);
        double precipitation = 0.5;
        return new WeatherSnapshot(
                locationName,
                latitude,
                longitude,
                temperature,
                windSpeed,
                precipitation,
                "fallback-weather",
                calculateImpact(temperature, windSpeed, precipitation),
                "fallback: " + reason
        );
    }

    private int calculateImpact(double temperature, double windSpeed, double precipitation) {
        double impact = precipitation * 18.0 + windSpeed * 1.3;
        if (temperature < 0 || temperature > 35) {
            impact += 12.0;
        }
        return TrafficMath.clampInt((int) Math.round(impact), 0, 100);
    }

    private String weatherDescription(int weatherCode) {
        return switch (weatherCode) {
            case 0 -> "clear";
            case 1, 2 -> "partly-cloudy";
            case 3 -> "overcast";
            case 45, 48 -> "fog";
            case 51, 53, 55 -> "drizzle";
            case 61, 63, 65 -> "rain";
            case 71, 73, 75 -> "snow";
            case 80, 81, 82 -> "showers";
            case 95, 96, 99 -> "thunderstorm";
            default -> "unknown";
        };
    }
}
