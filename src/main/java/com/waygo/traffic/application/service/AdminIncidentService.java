package com.waygo.traffic.application.service;

import com.waygo.report.application.port.outbound.UserReportRepository;
import com.waygo.report.domain.entity.UserReport;
import com.waygo.report.domain.valueobject.ReportStatus;
import com.waygo.report.domain.valueobject.ReportType;
import com.waygo.traffic.application.port.outbound.IncidentRealtimePublisher;
import com.waygo.traffic.application.port.outbound.TrafficAnomalyRepository;
import com.waygo.traffic.domain.entity.RoadIncident;
import com.waygo.traffic.domain.entity.TrafficAnomaly;
import com.waygo.traffic.domain.valueobject.AnomalyStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminIncidentService {

    private final UserReportRepository userReportRepository;
    private final TrafficAnomalyRepository trafficAnomalyRepository;
    private final IncidentRealtimePublisher incidentRealtimePublisher;

    public AdminIncidentService(UserReportRepository userReportRepository,
                                TrafficAnomalyRepository trafficAnomalyRepository,
                                IncidentRealtimePublisher incidentRealtimePublisher) {
        this.userReportRepository = userReportRepository;
        this.trafficAnomalyRepository = trafficAnomalyRepository;
        this.incidentRealtimePublisher = incidentRealtimePublisher;
    }

    public List<UserReport> getPendingReports() {
        return userReportRepository.findAll().stream()
                .filter(r -> r.status() == ReportStatus.PENDING)
                .collect(Collectors.toList());
    }

    public void approveReport(UUID reportId) {
        UserReport report = userReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (report.status() != ReportStatus.PENDING) {
            throw new IllegalStateException("Report is not PENDING");
        }

        UserReport approved = report.withStatus(ReportStatus.APPROVED);
        userReportRepository.save(approved);

        if (approved.type() == ReportType.ACCIDENT || approved.type() == ReportType.ROAD_CLOSED) {
            RoadIncident incident = new RoadIncident(
                    UUID.nameUUIDFromBytes((approved.userId() + approved.segmentId().toString() + approved.createdAt()).getBytes(StandardCharsets.UTF_8)),
                    approved.segmentId(),
                    approved.type().name(),
                    "USER_REPORT",
                    approved.description(),
                    approved.createdAt(),
                    true,
                    approved.latitude(),
                    approved.longitude()
            );
            trafficAnomalyRepository.save(new TrafficAnomaly(
                    approved.segmentId(),
                    approved.createdAt(),
                    3.0,
                    AnomalyStatus.ACTIVE,
                    "Admin approved user report confirmed a road incident"
            ));
            incidentRealtimePublisher.publishCreated(incident);
        }
    }

    public void rejectReport(UUID reportId) {
        UserReport report = userReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (report.status() != ReportStatus.PENDING) {
            throw new IllegalStateException("Report is not PENDING");
        }

        UserReport rejected = report.withStatus(ReportStatus.REJECTED);
        userReportRepository.save(rejected);
    }
}
