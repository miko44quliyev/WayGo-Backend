package com.waygo.traffic.application.usecase;

import com.waygo.weather.application.dto.WeatherQuery;
import com.waygo.weather.domain.entity.WeatherSnapshot;




public interface GetWeatherUseCase {

    WeatherSnapshot handle(WeatherQuery query);
}
