package waygo.application.port.in;

public interface ReceiveGpsPingUseCase {

    GpsPingReceipt handle(ReceiveGpsPingCommand command);
}
