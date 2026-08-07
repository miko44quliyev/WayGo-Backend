package com.waygo.report.application.usecase;

import com.waygo.report.domain.entity.UserReport;
import com.waygo.traffic.application.dto.SubmitReportCommand;
import com.waygo.report.application.port.outbound.UserReportRepository;
import com.waygo.traffic.application.port.outbound.IncidentRealtimePublisher;

import org.springframework.stereotype.Service;

@Service
public class SubmitReportUseCase {

    private final UserReportRepository userReportRepository;
    private final IncidentRealtimePublisher incidentRealtimePublisher;

    public SubmitReportUseCase(UserReportRepository userReportRepository, IncidentRealtimePublisher incidentRealtimePublisher) {
        this.userReportRepository = userReportRepository;
        this.incidentRealtimePublisher = incidentRealtimePublisher;
    }

    public UserReport handle(SubmitReportCommand command) {
        UserReport report = new UserReport(
                null,
                command.userId(),
                command.segmentId(),
                command.type(),
                command.description(),
                command.createdAt(),
                null, // Defaults to PENDING
                command.latitude(),
                command.longitude()
        );
        userReportRepository.save(report);

        // Publish to Admin UI
        incidentRealtimePublisher.publishReportPending(report);

        return report;
    }
}


