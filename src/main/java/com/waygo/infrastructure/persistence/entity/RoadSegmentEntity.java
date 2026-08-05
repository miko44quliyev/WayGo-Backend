package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "road_segment")
public class RoadSegmentEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String coordinatesJson;

    @Column(nullable = false)
    private String zone;

    public RoadSegmentEntity() {}

    public RoadSegmentEntity(UUID id, String name, String coordinatesJson, String zone) {
        this.id = id;
        this.name = name;
        this.coordinatesJson = coordinatesJson;
        this.zone = zone;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCoordinatesJson() { return coordinatesJson; }
    public void setCoordinatesJson(String coordinatesJson) { this.coordinatesJson = coordinatesJson; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
}
