package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.in;

import com.waygo.domain.traffic.TrafficAnomaly;

import java.util.List;

public interface GetAnomaliesUseCase {

    List<TrafficAnomaly> handle();
}
