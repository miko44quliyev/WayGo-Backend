package com.waygo.traffic.application.usecase;

import com.waygo.traffic.application.dto.PredictTrafficQuery;
import com.waygo.traffic.domain.entity.TrafficForecast;


public interface PredictTrafficUseCase {

    TrafficForecast handle(PredictTrafficQuery query);
}
