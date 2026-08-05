package com.waygo.application.service;

import com.waygo.domain.model.*;

import com.waygo.domain.model.AnomalyStatus;
import com.waygo.domain.model.ReportType;
import com.waygo.domain.model.TrafficAnomaly;
import com.waygo.domain.model.UserReport;
import com.waygo.infrastructure.persistence.repository.InMemoryTrafficAnomalyRepository;
import com.waygo.infrastructure.persistence.repository.InMemoryUserReportRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetIncidentsServiceTest {

    @Test
    void handleShouldMergeReportsAndAnomaliesInDescendingOrder() {
        InMemoryUserReportRepository reports = new InMemoryUserReportRepository();
        InMemoryTrafficAnomalyRepository anomalies = new InMemoryTrafficAnomalyRepository();

        reports.save(new UserReport(
                UUID.randomUUID(),
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                ReportType.ACCIDENT,
                "Accident",
                Instant.parse("2026-08-05T00:05:00Z")
        ));
        anomalies.save(new TrafficAnomaly(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                Instant.parse("2026-08-05T00:10:00Z"),
                -3.0,
                AnomalyStatus.ACTIVE,
                "Anomaly"
        ));

        GetIncidentsService service = new GetIncidentsService(reports, anomalies);
        var incidents = service.handle();

        assertEquals(2, incidents.size());
        assertEquals("STATISTICAL_ANOMALY", incidents.get(0).incidentType());
        assertEquals("ACCIDENT", incidents.get(1).incidentType());
    }
}
