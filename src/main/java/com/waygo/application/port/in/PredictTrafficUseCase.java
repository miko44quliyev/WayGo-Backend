package com.waygo.application.port.in;

import com.waygo.domain.model.*;



public interface PredictTrafficUseCase {

    TrafficForecast handle(PredictTrafficQuery query);
}
