package waygo.infrastructure.persistence;

import waygo.application.port.out.UserReportRepository;
import waygo.domain.model.UserReport;
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
