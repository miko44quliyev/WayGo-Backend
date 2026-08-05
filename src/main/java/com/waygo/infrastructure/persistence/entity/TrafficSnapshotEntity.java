package com.waygo.infrastructure.persistence.entity;

import com.waygo.domain.model.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "traffic_snapshot")
public class TrafficSnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID segmentId;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private double avgSpeed;

    @Column(nullable = false)
    private int congestionLevel;

    public TrafficSnapshotEntity() {}

    public TrafficSnapshotEntity(UUID segmentId, Instant timestamp, double avgSpeed, int congestionLevel) {
        this.segmentId = segmentId;
        this.timestamp = timestamp;
        this.avgSpeed = avgSpeed;
        this.congestionLevel = congestionLevel;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSegmentId() { return segmentId; }
    public void setSegmentId(UUID segmentId) { this.segmentId = segmentId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public double getAvgSpeed() { return avgSpeed; }
    public void setAvgSpeed(double avgSpeed) { this.avgSpeed = avgSpeed; }

    public int getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(int congestionLevel) { this.congestionLevel = congestionLevel; }
}
