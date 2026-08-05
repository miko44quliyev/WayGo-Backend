package com.waygo.infrastructure.external.tomtom;

import com.waygo.domain.model.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TomTomMapsGateway {

    @Value("${google.maps.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TomTomMapsGateway(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public String geocodeSearch(String query) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallbackSearch(query);
        }
        try {
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    URLEncoder.encode(query + " Baku Azerbaijan", StandardCharsets.UTF_8) +
                    "&key=" + apiKey;
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return fallbackSearch(query);
        }
    }

    public String calculateDirections(double fromLat, double fromLng, double toLat, double toLng, String mode) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallbackRoute(fromLat, fromLng, toLat, toLng);
        }
        try {
            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                    fromLat + "," + fromLng + "&destination=" + toLat + "," + toLng +
                    "&mode=driving&traffic_model=best_guess&departure_time=now&key=" + apiKey;
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return fallbackRoute(fromLat, fromLng, toLat, toLng);
        }
    }

    private String fallbackSearch(String query) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?format=json&q=" +
                    URLEncoder.encode(query + " Baku Azerbaijan", StandardCharsets.UTF_8) + "&limit=5";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return "[]";
        }
    }

    private String fallbackRoute(double fromLat, double fromLng, double toLat, double toLng) {
        try {
            String url = "https://router.project-osrm.org/route/v1/driving/" +
                    fromLng + "," + fromLat + ";" + toLng + "," + toLat + "?overview=full&geometries=geojson";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return "{}";
        }
    }
}
