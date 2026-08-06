package com.waygo.traffic.application.usecase;

import com.waygo.traffic.domain.entity.SmartEtaResult;




import java.util.List;
import java.util.UUID;

public interface CalculateSmartEtaUseCase {

    SmartEtaResult handle(List<UUID> segmentIds);
}
