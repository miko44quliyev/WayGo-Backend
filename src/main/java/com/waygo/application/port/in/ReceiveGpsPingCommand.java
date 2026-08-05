package com.waygo.application.port.in;

import com.waygo.domain.model.*;

import java.time.Instant;

public record ReceiveGpsPingCommand(String deviceId, double latitude, double longitude, Instant timestamp, double speedKmh) {
}
