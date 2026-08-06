package com.waygo.traffic.domain.entity;

import com.waygo.traffic.domain.valueobject.EtaWindow;

import java.util.List;

public record SmartEtaResult(
        List<EtaWindow> windows,
        double baselineTravelMinutes,
        String routeSummary
) {
}
