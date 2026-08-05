package com.waygo.application.port.in;

import com.waygo.domain.model.*;



public interface SubmitReportUseCase {

    UserReport handle(SubmitReportCommand command);
}
