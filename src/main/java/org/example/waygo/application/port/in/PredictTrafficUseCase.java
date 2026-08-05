package org.example.waygo.application.port.in;

import org.example.waygo.domain.model.TrafficForecast;

public interface PredictTrafficUseCase {

    TrafficForecast handle(PredictTrafficQuery query);
}
