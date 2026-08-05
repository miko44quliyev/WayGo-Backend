package waygo.application.port.out;

import waygo.domain.model.UserReport;

import java.util.List;

public interface UserReportRepository {

    void save(UserReport report);

    List<UserReport> findAll();
}
