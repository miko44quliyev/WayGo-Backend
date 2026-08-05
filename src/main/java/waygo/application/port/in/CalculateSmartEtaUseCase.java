package waygo.application.port.in;

import waygo.domain.model.SmartEtaResult;

import java.util.List;
import java.util.UUID;

public interface CalculateSmartEtaUseCase {

    SmartEtaResult handle(List<UUID> segmentIds);
}
