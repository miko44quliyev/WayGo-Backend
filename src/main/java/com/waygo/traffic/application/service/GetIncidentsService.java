package com.waygo.traffic.application.service;

import com.waygo.report.domain.entity.UserReport;
import com.waygo.traffic.domain.entity.RoadIncident;
import com.waygo.traffic.domain.entity.TrafficAnomaly;

import com.waygo.traffic.application.usecase.GetIncidentsUseCase;
import com.waygo.traffic.application.port.outbound.TrafficAnomalyRepository;
import com.waygo.report.application.port.outbound.UserReportRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

@Service
public class GetIncidentsService implements GetIncidentsUseCase {

    private final UserReportRepository userReportRepository;
    private final TrafficAnomalyRepository trafficAnomalyRepository;

    public GetIncidentsService(UserReportRepository userReportRepository, TrafficAnomalyRepository trafficAnomalyRepository) {
        this.userReportRepository = userReportRepository;
        this.trafficAnomalyRepository = trafficAnomalyRepository;
    }

    @Override
    public List<RoadIncident> handle() {
        List<RoadIncident> incidents = new ArrayList<>();
        for (UserReport report : userReportRepository.findAll()) {
            if (report.status() == com.waygo.report.domain.valueobject.ReportStatus.APPROVED) {
                incidents.add(new RoadIncident(
                        UUID.nameUUIDFromBytes((report.userId() + report.segmentId().toString() + report.createdAt()).getBytes(StandardCharsets.UTF_8)),
                        report.segmentId(),
                        report.type().name(),
                        "USER_REPORT",
                        report.description(),
                        report.createdAt(),
                        true,
                        report.latitude(),
                        report.longitude()
                ));
            }
        }
        for (TrafficAnomaly anomaly : trafficAnomalyRepository.findActive()) {
            incidents.add(new RoadIncident(
                    UUID.nameUUIDFromBytes((anomaly.segmentId() + anomaly.detectedAt().toString()).getBytes(StandardCharsets.UTF_8)),
                    anomaly.segmentId(),
                    "STATISTICAL_ANOMALY",
                    "ANOMALY_DETECTION",
                    anomaly.description(),
                    anomaly.detectedAt(),
                    true,
                    null,
                    null
            ));
        }
        incidents.sort(Comparator.comparing(RoadIncident::createdAt).reversed());
        return incidents;
    }
}
