package org.example.waygo.application.port.in;

import org.example.waygo.domain.model.UserReport;

public interface SubmitReportUseCase {

    UserReport handle(SubmitReportCommand command);
}
