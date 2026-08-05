package waygo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import waygo.domain.model.AnomalyStatus;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "anomaly_log")
public class TrafficAnomalyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID segmentId;

    @Column(nullable = false)
    private Instant detectedAt;

    @Column(nullable = false)
    private double zScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnomalyStatus status;

    @Column(columnDefinition = "TEXT")
    private String description;

    public TrafficAnomalyEntity() {}

    public TrafficAnomalyEntity(UUID segmentId, Instant detectedAt, double zScore, AnomalyStatus status, String description) {
        this.segmentId = segmentId;
        this.detectedAt = detectedAt;
        this.zScore = zScore;
        this.status = status;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSegmentId() { return segmentId; }
    public void setSegmentId(UUID segmentId) { this.segmentId = segmentId; }

    public Instant getDetectedAt() { return detectedAt; }
    public void setDetectedAt(Instant detectedAt) { this.detectedAt = detectedAt; }

    public double getZScore() { return zScore; }
    public void setZScore(double zScore) { this.zScore = zScore; }

    public AnomalyStatus getStatus() { return status; }
    public void setStatus(AnomalyStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
