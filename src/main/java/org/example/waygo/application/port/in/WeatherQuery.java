package org.example.waygo.application.port.in;

public record WeatherQuery(String locationName, double latitude, double longitude) {
}
