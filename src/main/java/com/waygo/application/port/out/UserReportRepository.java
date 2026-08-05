package com.waygo.application.port.out;

import com.waygo.domain.model.*;



import java.util.List;

public interface UserReportRepository {

    void save(UserReport report);

    List<UserReport> findAll();
}
