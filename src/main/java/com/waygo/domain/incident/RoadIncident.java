package com.waygo.domain.incident;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record RoadIncident(
        UUID id,
        UUID segmentId,
        String incidentType,
        String source,
        String description,
        Instant createdAt,
        boolean active
) {

    public RoadIncident {
        id = Objects.requireNonNull(id, "id");
        incidentType = Objects.requireNonNull(incidentType, "incidentType");
        source = Objects.requireNonNull(source, "source");
        description = Objects.requireNonNull(description, "description");
        createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }
}
