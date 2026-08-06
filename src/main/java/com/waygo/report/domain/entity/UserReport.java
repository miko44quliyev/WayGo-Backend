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

    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    protected UserReport() {}

    public UserReport(UUID id, UUID userId, UUID segmentId, ReportType type, String description, Instant createdAt, ReportStatus status, Double latitude, Double longitude) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.userId = userId;
        this.segmentId = segmentId;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status == null ? ReportStatus.PENDING : status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("id")
    public UUID id() { return id; }
    
    @com.fasterxml.jackson.annotation.JsonProperty("userId")
    public UUID userId() { return userId; }
    
    @com.fasterxml.jackson.annotation.JsonProperty("segmentId")
    public UUID segmentId() { return segmentId; }
    
    @com.fasterxml.jackson.annotation.JsonProperty("type")
    public ReportType type() { return type; }
    
    @com.fasterxml.jackson.annotation.JsonProperty("description")
    public String description() { return description; }
    
    @com.fasterxml.jackson.annotation.JsonProperty("createdAt")
    public Instant createdAt() { return createdAt; }
    
    @com.fasterxml.jackson.annotation.JsonProperty("status")
    public ReportStatus status() { return status; }

    @com.fasterxml.jackson.annotation.JsonProperty("latitude")
    public Double latitude() { return latitude; }

    @com.fasterxml.jackson.annotation.JsonProperty("longitude")
    public Double longitude() { return longitude; }

    public UserReport withStatus(ReportStatus newStatus) {
        return new UserReport(this.id, this.userId, this.segmentId, this.type, this.description, this.createdAt, newStatus, this.latitude, this.longitude);
    }
}
