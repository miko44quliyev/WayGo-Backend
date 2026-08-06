package com.waygo.traffic.application.service;

import com.waygo.weather.domain.entity.WeatherSnapshot;


import com.waygo.traffic.application.usecase.GetWeatherUseCase;
import com.waygo.weather.application.dto.WeatherQuery;
import com.waygo.weather.application.port.outbound.WeatherGateway;

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
