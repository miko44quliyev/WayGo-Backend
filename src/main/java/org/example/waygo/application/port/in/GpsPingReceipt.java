package org.example.waygo.application.port.in;

import org.example.waygo.domain.model.GpsPing;
import org.example.waygo.domain.model.RoadSegment;
import org.example.waygo.domain.model.TrafficAnomaly;
import org.example.waygo.domain.model.TrafficSnapshot;

public record GpsPingReceipt(GpsPing ping, RoadSegment segment, TrafficSnapshot snapshot, TrafficAnomaly anomaly) {
}
