package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.domain.traffic;

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
