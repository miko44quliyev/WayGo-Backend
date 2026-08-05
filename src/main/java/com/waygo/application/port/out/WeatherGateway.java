package com.waygo.application.port.out;

import com.waygo.domain.model.*;



public interface WeatherGateway {

    WeatherSnapshot fetch(String locationName, double latitude, double longitude);

    WeatherSnapshot fallback(String locationName, double latitude, double longitude, String reason);
}
