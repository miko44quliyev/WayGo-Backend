package waygo.application.port.in;

import waygo.domain.model.GpsPing;
import waygo.domain.model.RoadSegment;
import waygo.domain.model.TrafficAnomaly;
import waygo.domain.model.TrafficSnapshot;

public record GpsPingReceipt(GpsPing ping, RoadSegment segment, TrafficSnapshot snapshot, TrafficAnomaly anomaly) {
}
