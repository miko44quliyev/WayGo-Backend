package com.waygo.report.application.usecase;

import com.waygo.report.domain.entity.UserReport;
import com.waygo.traffic.application.dto.SubmitReportCommand;




public interface SubmitReportUseCase {

    UserReport handle(SubmitReportCommand command);
}
