package com.waygo.weather.application.port.outbound;

import com.waygo.weather.domain.entity.WeatherSnapshot;




public interface WeatherGateway {

    WeatherSnapshot fetch(String locationName, double latitude, double longitude);

    WeatherSnapshot fallback(String locationName, double latitude, double longitude, String reason);
}
