package com.waygo.traffic.domain.valueobject;

import java.time.Instant;
import java.util.List;

public record EtaWindow(
        int departureOffsetMinutes,
        Instant departureAt,
        double travelMinutes,
        Instant arrivalAt,
        List<String> segmentBreakdown
) {
}
