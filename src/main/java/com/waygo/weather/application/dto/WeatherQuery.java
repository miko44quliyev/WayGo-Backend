package com.waygo.weather.application.dto;


public record WeatherQuery(String locationName, double latitude, double longitude) {
}
