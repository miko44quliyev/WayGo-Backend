package com.waygo.application.port.in;

import com.waygo.domain.model.*;

public record WeatherQuery(String locationName, double latitude, double longitude) {
}
