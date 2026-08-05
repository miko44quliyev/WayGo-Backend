package org.example.waygo.application.service;

import org.example.waygo.application.port.in.SubmitReportCommand;
import org.example.waygo.application.port.in.SubmitReportUseCase;
import org.example.waygo.application.port.out.TrafficAnomalyRepository;
import org.example.waygo.application.port.out.UserReportRepository;
import org.example.waygo.domain.model.AnomalyStatus;
import org.example.waygo.domain.model.ReportType;
import org.example.waygo.domain.model.TrafficAnomaly;
import org.example.waygo.domain.model.UserReport;
import org.example.waygo.infrastructure.support.TrafficMath;
import org.springframework.stereotype.Service;

@Service
public class SubmitReportService implements SubmitReportUseCase {

    private final UserReportRepository userReportRepository;
    private final TrafficAnomalyRepository trafficAnomalyRepository;

    public SubmitReportService(UserReportRepository userReportRepository, TrafficAnomalyRepository trafficAnomalyRepository) {
        this.userReportRepository = userReportRepository;
        this.trafficAnomalyRepository = trafficAnomalyRepository;
    }

    @Override
    public UserReport handle(SubmitReportCommand command) {
        UserReport report = new UserReport(command.userId(), command.segmentId(), command.type(), command.description(), command.createdAt());
        userReportRepository.save(report);

        if (command.type() == ReportType.ACCIDENT || command.type() == ReportType.ROAD_CLOSED) {
            trafficAnomalyRepository.save(new TrafficAnomaly(
                    command.segmentId(),
                    command.createdAt(),
                    3.0,
                    AnomalyStatus.ACTIVE,
                    "User report confirmed a road incident"
            ));
        }

        return report;
    }
}
