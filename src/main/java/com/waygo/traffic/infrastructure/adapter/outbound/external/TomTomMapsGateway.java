package com.waygo.traffic.infrastructure.adapter.outbound.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TomTomMapsGateway {

    @Value("${tomtom.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TomTomMapsGateway(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public String geocodeSearch(String query) {
        try {
            String url = "https://api.tomtom.com/search/2/geocode/" +
                    URLEncoder.encode(query, StandardCharsets.UTF_8) + ".json?key=" + getActiveApiKey() +
                    "&lat=40.4093&lon=49.8671&radius=50000&countrySet=AZ&limit=5";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return "{\"results\": []}";
        }
    }

    public String calculateDirections(double fromLat, double fromLng, double toLat, double toLng, String mode) {
        try {
            String routeType = "shortest".equalsIgnoreCase(mode) ? "shortest" : "fastest";
            String url = "https://api.tomtom.com/routing/1/calculateRoute/" +
                    fromLat + "," + fromLng + ":" + toLat + "," + toLng +
                    "/json?key=" + getActiveApiKey() + "&routeType=" + routeType +
                    "&maxAlternatives=2&routeRepresentation=polyline&computeTravelTimeFor=all";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return "{\"routes\": []}";
        }
    }

    public String fetchRealIncidents(double minLng, double minLat, double maxLng, double maxLat) {
        try {
            String url = "https://api.tomtom.com/traffic/services/5/incidentDetails?key=" + getActiveApiKey() +
                    "&bbox=" + minLng + "," + minLat + "," + maxLng + "," + maxLat +
                    "&fields={incidents{type,geometry{type,coordinates},properties{iconCategory,magnitudeOfDelay,events{description,code}}}}" +
                    "&language=az-AZ";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            return "{\"incidents\": []}";
        }
    }

    public byte[] proxyBasicTile(int z, int x, int y) {
        try {
            String url = "https://api.tomtom.com/map/1/tile/basic/main/" + z + "/" + x + "/" + y + ".png?key=" + getActiveApiKey();
            return restTemplate.getForObject(url, byte[].class);
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    public byte[] proxyTrafficFlowTile(int z, int x, int y) {
        try {
            String url = "https://api.tomtom.com/traffic/map/4/tile/flow/relative0/" + z + "/" + x + "/" + y + ".png?key=" + getActiveApiKey();
            return restTemplate.getForObject(url, byte[].class);
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    public byte[] proxyTrafficIncidentTile(int z, int x, int y) {
        try {
            String url = "https://api.tomtom.com/traffic/map/4/tile/incidents/s3/" + z + "/" + x + "/" + y + ".png?key=" + getActiveApiKey();
            return restTemplate.getForObject(url, byte[].class);
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    private String getActiveApiKey() {
        if (apiKey != null && !apiKey.isBlank()) {
            return apiKey;
        }
        return System.getenv().getOrDefault("TOMTOM_API_KEY", "");
    }
}
