package com.waygo.traffic.application.port.outbound;

import com.waygo.traffic.domain.entity.RoadSegment;




import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoadSegmentRepository {

    List<RoadSegment> findAll();

    Optional<RoadSegment> findById(UUID id);

    void saveAll(Collection<RoadSegment> segments);
}
