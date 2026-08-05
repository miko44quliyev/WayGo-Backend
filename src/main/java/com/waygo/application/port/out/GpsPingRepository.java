package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.out;

import com.waygo.domain.traffic.GpsPing;

import java.util.List;

public interface GpsPingRepository {

    void save(GpsPing ping);

    List<GpsPing> findAll();
}
