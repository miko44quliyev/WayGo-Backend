package com.waygo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@Table(name = "historical_pattern")
public class HistoricalPatternEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID segmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private int hour;

    @Column(nullable = false)
    private double avgSpeed;

    @Column(nullable = false)
    private double stdDev;

    public HistoricalPatternEntity() {}

    public HistoricalPatternEntity(UUID segmentId, DayOfWeek dayOfWeek, int hour, double avgSpeed, double stdDev) {
        this.segmentId = segmentId;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.avgSpeed = avgSpeed;
        this.stdDev = stdDev;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSegmentId() { return segmentId; }
    public void setSegmentId(UUID segmentId) { this.segmentId = segmentId; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public int getHour() { return hour; }
    public void setHour(int hour) { this.hour = hour; }

    public double getAvgSpeed() { return avgSpeed; }
    public void setAvgSpeed(double avgSpeed) { this.avgSpeed = avgSpeed; }

    public double getStdDev() { return stdDev; }
    public void setStdDev(double stdDev) { this.stdDev = stdDev; }
}
