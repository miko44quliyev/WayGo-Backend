package com.waygo.report.application.port.outbound;

import com.waygo.report.domain.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, UUID> {
}
