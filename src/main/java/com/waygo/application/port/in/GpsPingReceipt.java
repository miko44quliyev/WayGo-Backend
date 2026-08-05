package C:.Users.mikoq.Downloads.waygo.WayGo-Backend.src.main.java.com.waygo.application.port.in;

import com.waygo.domain.traffic.GpsPing;
import com.waygo.domain.traffic.RoadSegment;
import com.waygo.domain.traffic.TrafficAnomaly;
import com.waygo.domain.traffic.TrafficSnapshot;

public record GpsPingReceipt(GpsPing ping, RoadSegment segment, TrafficSnapshot snapshot, TrafficAnomaly anomaly) {
}
