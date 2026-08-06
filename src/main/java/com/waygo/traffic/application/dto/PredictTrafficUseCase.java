package com.waygo.traffic.application.dto;

import com.waygo.traffic.domain.entity.TrafficForecast;




public interface PredictTrafficUseCase {

    TrafficForecast handle(PredictTrafficQuery query);
}
