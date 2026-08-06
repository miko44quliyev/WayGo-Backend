package com.waygo.report.infrastructure.adapter.outbound.persistence;

import com.waygo.report.domain.entity.UserReport;


import com.waygo.report.application.port.outbound.UserReportRepository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryUserReportRepository implements UserReportRepository {

    private final CopyOnWriteArrayList<UserReport> storage = new CopyOnWriteArrayList<>();

    @Override
    public void save(UserReport report) {
        storage.add(report);
    }

    @Override
    public List<UserReport> findAll() {
        return new ArrayList<>(storage);
    }
}
