package com.waygo.application.port.out;

import com.waygo.domain.model.*;



import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoadSegmentRepository {

    List<RoadSegment> findAll();

    Optional<RoadSegment> findById(UUID id);

    void saveAll(Collection<RoadSegment> segments);
}
