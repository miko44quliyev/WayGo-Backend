package org.example.waygo.application.port.out;

import org.example.waygo.domain.model.UserReport;

import java.util.List;

public interface UserReportRepository {

    void save(UserReport report);

    List<UserReport> findAll();
}
