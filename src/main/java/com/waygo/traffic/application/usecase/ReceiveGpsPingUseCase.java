package com.waygo.traffic.application.usecase;

import com.waygo.traffic.application.dto.GpsPingReceipt;
import com.waygo.traffic.application.dto.ReceiveGpsPingCommand;


public interface ReceiveGpsPingUseCase {

    GpsPingReceipt handle(ReceiveGpsPingCommand command);
}
