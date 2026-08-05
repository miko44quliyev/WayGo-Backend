package org.example.waygo.application.port.in;

import org.example.waygo.domain.model.SmartEtaResult;

import java.util.List;
import java.util.UUID;

public interface CalculateSmartEtaUseCase {

    SmartEtaResult handle(List<UUID> segmentIds);
}
