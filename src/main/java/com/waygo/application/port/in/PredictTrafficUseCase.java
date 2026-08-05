package com.waygo.application.port.in;

import com.waygo.domain.traffic.TrafficForecast;

public interface PredictTrafficUseCase {

    TrafficForecast handle(PredictTrafficQuery query);
}
