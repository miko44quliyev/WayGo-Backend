package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.in;

import com.waygo.domain.traffic.UserReport;

public interface SubmitReportUseCase {

    UserReport handle(SubmitReportCommand command);
}
