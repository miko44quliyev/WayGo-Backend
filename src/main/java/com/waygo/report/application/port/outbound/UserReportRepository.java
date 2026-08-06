package com.waygo.report.application.port.outbound;

import com.waygo.report.domain.entity.UserReport;




import java.util.List;

public interface UserReportRepository {

    void save(UserReport report);

    List<UserReport> findAll();
}
