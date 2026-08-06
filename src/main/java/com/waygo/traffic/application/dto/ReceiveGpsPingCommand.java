package com.waygo.traffic.application.dto;


import java.time.Instant;

public record ReceiveGpsPingCommand(String deviceId, double latitude, double longitude, Instant timestamp, double speedKmh) {
}
