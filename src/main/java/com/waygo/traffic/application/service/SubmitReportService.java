package com.waygo.traffic.application.service;

import com.waygo.report.domain.entity.UserReport;
import com.waygo.traffic.application.dto.SubmitReportCommand;
import com.waygo.report.application.usecase.SubmitReportUseCase;
import com.waygo.report.application.port.outbound.UserReportRepository;

import org.springframework.stereotype.Service;

@Service
public class SubmitReportService implements SubmitReportUseCase {

    private final UserReportRepository userReportRepository;

    public SubmitReportService(UserReportRepository userReportRepository) {
        this.userReportRepository = userReportRepository;
    }

    @Override
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

        // We no longer publish to IncidentRealtimePublisher here. 
        // This will be done when an Admin approves the report.

        return report;
    }
}
