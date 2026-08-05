package com.waygo.application.port.in;

import com.waygo.domain.traffic.WeatherSnapshot;

public interface GetWeatherUseCase {

    WeatherSnapshot handle(WeatherQuery query);
}
