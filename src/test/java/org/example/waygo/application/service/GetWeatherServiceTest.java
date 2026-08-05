package org.example.waygo.application.service;

import org.example.waygo.application.port.in.WeatherQuery;
import org.example.waygo.application.port.out.WeatherGateway;
import org.example.waygo.domain.model.WeatherSnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetWeatherServiceTest {

    @Test
    void handleShouldReturnGatewayResponse() {
        WeatherGateway gateway = mock(WeatherGateway.class);
        WeatherSnapshot snapshot = new WeatherSnapshot("Baku", 40.0, 49.0, 20.0, 5.0, 0.0, "clear", 2, "open-meteo");
        when(gateway.fetch("Baku", 40.0, 49.0)).thenReturn(snapshot);

        GetWeatherService service = new GetWeatherService(gateway);
        WeatherSnapshot actual = service.handle(new WeatherQuery("Baku", 40.0, 49.0));

        assertEquals(snapshot, actual);
        verify(gateway).fetch("Baku", 40.0, 49.0);
    }

    @Test
    void handleShouldFallbackOnRestException() {
        WeatherGateway gateway = mock(WeatherGateway.class);
        WeatherSnapshot fallback = new WeatherSnapshot("Baku", 40.0, 49.0, 19.0, 6.0, 0.0, "fallback-weather", 4, "fallback");
        doThrow(new RestClientException("boom")).when(gateway).fetch("Baku", 40.0, 49.0);
        when(gateway.fallback("Baku", 40.0, 49.0, "boom")).thenReturn(fallback);

        GetWeatherService service = new GetWeatherService(gateway);
        WeatherSnapshot actual = service.handle(new WeatherQuery("Baku", 40.0, 49.0));

        assertEquals(fallback, actual);
        verify(gateway).fallback("Baku", 40.0, 49.0, "boom");
    }
}
