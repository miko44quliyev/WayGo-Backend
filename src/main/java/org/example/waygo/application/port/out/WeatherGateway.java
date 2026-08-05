package org.example.waygo.application.port.out;

import org.example.waygo.domain.model.WeatherSnapshot;

public interface WeatherGateway {

    WeatherSnapshot fetch(String locationName, double latitude, double longitude);

    WeatherSnapshot fallback(String locationName, double latitude, double longitude, String reason);
}
