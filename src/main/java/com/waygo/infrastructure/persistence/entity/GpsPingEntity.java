package com.waygo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "gps_ping")
public class GpsPingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private double speedKmh;

    public GpsPingEntity() {}

    public GpsPingEntity(String deviceId, double latitude, double longitude, Instant timestamp, double speedKmh) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.speedKmh = speedKmh;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public double getSpeedKmh() { return speedKmh; }
    public void setSpeedKmh(double speedKmh) { this.speedKmh = speedKmh; }
}
