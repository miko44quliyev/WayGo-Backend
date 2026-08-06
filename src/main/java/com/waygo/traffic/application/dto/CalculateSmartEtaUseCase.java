package com.waygo.traffic.application.dto;

import com.waygo.traffic.domain.entity.SmartEtaResult;




import java.util.List;
import java.util.UUID;

public interface CalculateSmartEtaUseCase {

    SmartEtaResult handle(List<UUID> segmentIds);
}
