package org.example.waygo.application.service;

import org.example.waygo.application.port.in.SubmitReportCommand;
import org.example.waygo.application.port.in.SubmitReportUseCase;
import org.example.waygo.application.port.out.IncidentRealtimePublisher;
import org.example.waygo.application.port.out.TrafficAnomalyRepository;
import org.example.waygo.application.port.out.UserReportRepository;
import org.example.waygo.domain.model.AnomalyStatus;
import org.example.waygo.domain.model.ReportType;
import org.example.waygo.domain.model.RoadIncident;
import org.example.waygo.domain.model.TrafficAnomaly;
import org.example.waygo.domain.model.UserReport;
import org.springframework.stereotype.Service;

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
                    UUID.nameUUIDFromBytes((command.userId() + command.segmentId().toString() + command.createdAt()).getBytes()),
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
