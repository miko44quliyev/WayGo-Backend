package com.waygo.traffic.application.dto;


import java.time.DayOfWeek;
import java.util.UUID;

public record PredictTrafficQuery(UUID segmentId, DayOfWeek dayOfWeek, int hour) {
}
