package org.example.waygo.domain.model;

import java.time.Instant;
import java.util.UUID;

public record IncidentEvent(
        String eventType,
        UUID id,
        UUID segmentId,
        String incidentType,
        String source,
        String description,
        String createdAt,
        boolean active
) {
    public static IncidentEvent from(RoadIncident incident) {
        return new IncidentEvent(
                "incident:created",
                incident.id(),
                incident.segmentId(),
                incident.incidentType(),
                incident.source(),
                incident.description(),
                incident.createdAt().toString(),
                incident.active()
        );
    }
}
