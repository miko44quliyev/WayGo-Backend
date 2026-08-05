package com.waygo.application.port.in;

import com.waygo.domain.model.*;



public interface GetWeatherUseCase {

    WeatherSnapshot handle(WeatherQuery query);
}
