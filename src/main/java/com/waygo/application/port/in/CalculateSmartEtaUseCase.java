package com.waygo.application.port.in;

import com.waygo.domain.traffic.SmartEtaResult;

import java.util.List;
import java.util.UUID;

public interface CalculateSmartEtaUseCase {

    SmartEtaResult handle(List<UUID> segmentIds);
}
