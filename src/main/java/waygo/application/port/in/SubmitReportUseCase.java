package waygo.application.port.in;

import waygo.domain.model.UserReport;

public interface SubmitReportUseCase {

    UserReport handle(SubmitReportCommand command);
}
