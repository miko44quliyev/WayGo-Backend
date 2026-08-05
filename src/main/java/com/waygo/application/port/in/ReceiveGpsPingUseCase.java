package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.in;

public interface ReceiveGpsPingUseCase {

    GpsPingReceipt handle(ReceiveGpsPingCommand command);
}
