package org.example.waygo.domain.model;

import java.util.List;

public record SmartEtaResult(
        List<EtaWindow> windows,
        double baselineTravelMinutes,
        String routeSummary
) {
}
