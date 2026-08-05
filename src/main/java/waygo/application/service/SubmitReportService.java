package waygo.application.service;

import waygo.application.port.in.SubmitReportCommand;
import waygo.application.port.in.SubmitReportUseCase;
import waygo.application.port.out.IncidentRealtimePublisher;
import waygo.application.port.out.TrafficAnomalyRepository;
import waygo.application.port.out.UserReportRepository;
import waygo.domain.model.AnomalyStatus;
import waygo.domain.model.ReportType;
import waygo.domain.model.RoadIncident;
import waygo.domain.model.TrafficAnomaly;
import waygo.domain.model.UserReport;
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
