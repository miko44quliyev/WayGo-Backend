package com.waygo.application.port.in;

import com.waygo.domain.model.*;






public record GpsPingReceipt(GpsPing ping, RoadSegment segment, TrafficSnapshot snapshot, TrafficAnomaly anomaly) {
}
