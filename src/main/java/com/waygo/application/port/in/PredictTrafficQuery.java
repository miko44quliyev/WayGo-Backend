package com.waygo.application.port.in;

import java.time.DayOfWeek;
import java.util.UUID;

public record PredictTrafficQuery(UUID segmentId, DayOfWeek dayOfWeek, int hour) {
}
