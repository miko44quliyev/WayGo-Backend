package waygo.application.service;

import waygo.application.port.in.GetIncidentsUseCase;
import waygo.application.port.out.TrafficAnomalyRepository;
import waygo.application.port.out.UserReportRepository;
import waygo.domain.model.RoadIncident;
import waygo.domain.model.TrafficAnomaly;
import waygo.domain.model.UserReport;
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
            incidents.add(new RoadIncident(
                    UUID.nameUUIDFromBytes((report.userId() + report.segmentId().toString() + report.createdAt()).getBytes(StandardCharsets.UTF_8)),
                    report.segmentId(),
                    report.type().name(),
                    "USER_REPORT",
                    report.description(),
                    report.createdAt(),
                    true
            ));
        }
        for (TrafficAnomaly anomaly : trafficAnomalyRepository.findActive()) {
            incidents.add(new RoadIncident(
                    UUID.nameUUIDFromBytes((anomaly.segmentId() + anomaly.detectedAt().toString()).getBytes(StandardCharsets.UTF_8)),
                    anomaly.segmentId(),
                    "STATISTICAL_ANOMALY",
                    "ANOMALY_DETECTION",
                    anomaly.description(),
                    anomaly.detectedAt(),
                    true
            ));
        }
        incidents.sort(Comparator.comparing(RoadIncident::createdAt).reversed());
        return incidents;
    }
}
