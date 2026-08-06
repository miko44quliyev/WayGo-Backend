package com.waygo.traffic.application.dto;

import com.waygo.traffic.domain.entity.GpsPing;
import com.waygo.traffic.domain.entity.RoadSegment;
import com.waygo.traffic.domain.entity.TrafficAnomaly;
import com.waygo.traffic.domain.entity.TrafficSnapshot;







public record GpsPingReceipt(GpsPing ping, RoadSegment segment, TrafficSnapshot snapshot, TrafficAnomaly anomaly) {
}
