package org.example.waygo.application.port.in;

public interface ReceiveGpsPingUseCase {

    GpsPingReceipt handle(ReceiveGpsPingCommand command);
}
