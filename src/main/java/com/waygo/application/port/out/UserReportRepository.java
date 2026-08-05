package com.waygo.application.port.out;

import com.waygo.domain.traffic.UserReport;

import java.util.List;

public interface UserReportRepository {

    void save(UserReport report);

    List<UserReport> findAll();
}
