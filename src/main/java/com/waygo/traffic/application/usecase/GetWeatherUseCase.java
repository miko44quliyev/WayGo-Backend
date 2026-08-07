package com.waygo.traffic.application.usecase;

import com.waygo.weather.domain.entity.WeatherSnapshot;


import com.waygo.traffic.application.usecase.GetWeatherUseCase;
import com.waygo.weather.application.dto.WeatherQuery;
import com.waygo.weather.application.port.outbound.WeatherGateway;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClientException;

@Service
public class GetWeatherUseCase {

    private final WeatherGateway weatherGateway;

    public GetWeatherUseCase(WeatherGateway weatherGateway) {
        this.weatherGateway = weatherGateway;
    }

    public WeatherSnapshot handle(WeatherQuery query) {
        try {
            return weatherGateway.fetch(query.locationName(), query.latitude(), query.longitude());
        } catch (RestClientException ex) {
            return weatherGateway.fallback(query.locationName(), query.latitude(), query.longitude(), ex.getMessage());
        }
    }
}


