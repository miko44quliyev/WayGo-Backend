package com.waygo.traffic.application.dto;


public interface ReceiveGpsPingUseCase {

    GpsPingReceipt handle(ReceiveGpsPingCommand command);
}
