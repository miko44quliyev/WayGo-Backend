package com.waygo.application.port.in;

import com.waygo.domain.model.*;

public interface ReceiveGpsPingUseCase {

    GpsPingReceipt handle(ReceiveGpsPingCommand command);
}
