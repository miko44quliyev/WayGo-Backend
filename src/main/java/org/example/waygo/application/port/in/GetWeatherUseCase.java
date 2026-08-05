package org.example.waygo.application.port.in;

import org.example.waygo.domain.model.WeatherSnapshot;

public interface GetWeatherUseCase {

    WeatherSnapshot handle(WeatherQuery query);
}
