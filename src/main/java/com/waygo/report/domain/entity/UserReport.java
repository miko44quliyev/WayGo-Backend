package com.waygo.report.domain.entity;

import com.waygo.report.domain.valueobject.ReportStatus;
import com.waygo.report.domain.valueobject.ReportType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_reports")
public class UserReport {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID segmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    protected UserReport() {}

    public UserReport(UUID id, UUID userId, UUID segmentId, ReportType type, String description, Instant createdAt, ReportStatus status) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.userId = userId;
        this.segmentId = segmentId;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status == null ? ReportStatus.PENDING : status;
    }

    public UUID id() { return id; }
    public UUID userId() { return userId; }
    public UUID segmentId() { return segmentId; }
    public ReportType type() { return type; }
    public String description() { return description; }
    public Instant createdAt() { return createdAt; }
    public ReportStatus status() { return status; }

    public UserReport withStatus(ReportStatus newStatus) {
        return new UserReport(this.id, this.userId, this.segmentId, this.type, this.description, this.createdAt, newStatus);
    }
}
