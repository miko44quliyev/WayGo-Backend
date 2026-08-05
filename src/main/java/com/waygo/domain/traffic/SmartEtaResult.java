package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.domain.traffic;

import java.util.List;

public record SmartEtaResult(
        List<EtaWindow> windows,
        double baselineTravelMinutes,
        String routeSummary
) {
}
