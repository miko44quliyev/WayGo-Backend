package com.waygo.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record RoadSegment(UUID id, String name, List<Coordinate> coordinates, String zone) {

    public RoadSegment {
        id = Objects.requireNonNull(id, "id");
        name = Objects.requireNonNull(name, "name");
        coordinates = List.copyOf(Objects.requireNonNull(coordinates, "coordinates"));
        zone = Objects.requireNonNull(zone, "zone");
        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("coordinates must not be empty");
        }
    }

    public Coordinate centroid() {
        double latitudeSum = 0.0;
        double longitudeSum = 0.0;
        for (Coordinate coordinate : coordinates) {
            latitudeSum += coordinate.latitude();
            longitudeSum += coordinate.longitude();
        }
        return new Coordinate(latitudeSum / coordinates.size(), longitudeSum / coordinates.size());
    }
}
