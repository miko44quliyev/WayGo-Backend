package com.waygo.traffic.application.service;

import com.waygo.report.domain.entity.UserReport;
import com.waygo.report.domain.valueobject.ReportType;
import com.waygo.traffic.domain.entity.RoadIncident;
import com.waygo.traffic.domain.entity.TrafficAnomaly;
import com.waygo.traffic.domain.valueobject.AnomalyStatus;


import com.waygo.traffic.application.dto.SubmitReportCommand;
import com.waygo.report.application.usecase.SubmitReportUseCase;
import com.waygo.traffic.application.port.outbound.IncidentRealtimePublisher;
import com.waygo.traffic.application.port.outbound.TrafficAnomalyRepository;
import com.waygo.report.application.port.outbound.UserReportRepository;





import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class SubmitReportService implements SubmitReportUseCase {

    private final UserReportRepository userReportRepository;
    private final TrafficAnomalyRepository trafficAnomalyRepository;
    private final IncidentRealtimePublisher incidentRealtimePublisher;

    public SubmitReportService(
            UserReportRepository userReportRepository,
            TrafficAnomalyRepository trafficAnomalyRepository,
            IncidentRealtimePublisher incidentRealtimePublisher
    ) {
        this.userReportRepository = userReportRepository;
        this.trafficAnomalyRepository = trafficAnomalyRepository;
        this.incidentRealtimePublisher = incidentRealtimePublisher;
    }

    @Override
    public UserReport handle(SubmitReportCommand command) {
        UserReport report = new UserReport(command.userId(), command.segmentId(), command.type(), command.description(), command.createdAt());
        userReportRepository.save(report);

        if (command.type() == ReportType.ACCIDENT || command.type() == ReportType.ROAD_CLOSED) {
            RoadIncident incident = new RoadIncident(
                    UUID.nameUUIDFromBytes((command.userId() + command.segmentId().toString() + command.createdAt()).getBytes(StandardCharsets.UTF_8)),
                    command.segmentId(),
                    command.type().name(),
                    "USER_REPORT",
                    command.description(),
                    command.createdAt(),
                    true
            );
            trafficAnomalyRepository.save(new TrafficAnomaly(
                    command.segmentId(),
                    command.createdAt(),
                    3.0,
                    AnomalyStatus.ACTIVE,
                    "User report confirmed a road incident"
            ));
            incidentRealtimePublisher.publishCreated(incident);
        }

        return report;
    }
}
