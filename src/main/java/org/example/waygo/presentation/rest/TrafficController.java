package org.example.waygo.presentation.rest;

import org.example.waygo.application.port.in.GetAnomaliesUseCase;
import org.example.waygo.application.port.in.GetCityStatsUseCase;
import org.example.waygo.application.port.in.GetIncidentsUseCase;
import org.example.waygo.application.port.in.GetTrafficMapUseCase;
import org.example.waygo.application.port.in.GetWeatherUseCase;
import org.example.waygo.application.port.in.PredictTrafficQuery;
import org.example.waygo.application.port.in.PredictTrafficUseCase;
import org.example.waygo.application.port.in.ReceiveGpsPingCommand;
import org.example.waygo.application.port.in.ReceiveGpsPingUseCase;
import org.example.waygo.application.port.in.WeatherQuery;
import org.example.waygo.application.port.in.CalculateSmartEtaUseCase;
import org.example.waygo.application.port.in.SubmitReportCommand;
import org.example.waygo.application.port.in.SubmitReportUseCase;
import org.example.waygo.domain.model.CityStats;
import org.example.waygo.domain.model.GpsPing;
import org.example.waygo.domain.model.ReportType;
import org.example.waygo.domain.model.RoadIncident;
import org.example.waygo.domain.model.SmartEtaResult;
import org.example.waygo.domain.model.TrafficForecast;
import org.example.waygo.domain.model.TrafficMapView;
import org.example.waygo.domain.model.TrafficAnomaly;
import org.example.waygo.domain.model.UserReport;
import org.example.waygo.domain.model.WeatherSnapshot;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api", "/api/v1"})
public class TrafficController {

    private final ReceiveGpsPingUseCase receiveGpsPingUseCase;
    private final GetTrafficMapUseCase getTrafficMapUseCase;
    private final PredictTrafficUseCase predictTrafficUseCase;
    private final GetAnomaliesUseCase getAnomaliesUseCase;
    private final SubmitReportUseCase submitReportUseCase;
    private final GetIncidentsUseCase getIncidentsUseCase;
    private final GetCityStatsUseCase getCityStatsUseCase;
    private final GetWeatherUseCase getWeatherUseCase;
    private final CalculateSmartEtaUseCase calculateSmartEtaUseCase;

    public TrafficController(
            ReceiveGpsPingUseCase receiveGpsPingUseCase,
            GetTrafficMapUseCase getTrafficMapUseCase,
            PredictTrafficUseCase predictTrafficUseCase,
            GetAnomaliesUseCase getAnomaliesUseCase,
            SubmitReportUseCase submitReportUseCase,
            GetIncidentsUseCase getIncidentsUseCase,
            GetCityStatsUseCase getCityStatsUseCase,
            GetWeatherUseCase getWeatherUseCase,
            CalculateSmartEtaUseCase calculateSmartEtaUseCase
    ) {
        this.receiveGpsPingUseCase = receiveGpsPingUseCase;
        this.getTrafficMapUseCase = getTrafficMapUseCase;
        this.predictTrafficUseCase = predictTrafficUseCase;
        this.getAnomaliesUseCase = getAnomaliesUseCase;
        this.submitReportUseCase = submitReportUseCase;
        this.getIncidentsUseCase = getIncidentsUseCase;
        this.getCityStatsUseCase = getCityStatsUseCase;
        this.getWeatherUseCase = getWeatherUseCase;
        this.calculateSmartEtaUseCase = calculateSmartEtaUseCase;
    }

    @PostMapping("/gps-ping")
    public ResponseEntity<GpsPingReceiptResponse> receiveGpsPing(@Valid @RequestBody GpsPingRequest request) {
        var result = receiveGpsPingUseCase.handle(new ReceiveGpsPingCommand(
                request.deviceId(),
                request.latitude(),
                request.longitude(),
                request.timestamp(),
                request.speedKmh()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(GpsPingReceiptResponse.from(result));
    }

    @GetMapping("/traffic-map")
    public TrafficMapView getTrafficMap() {
        return getTrafficMapUseCase.handle();
    }

    @GetMapping("/predict")
    public TrafficForecast predictTraffic(
            @RequestParam UUID segmentId,
            @RequestParam(defaultValue = "MONDAY") DayOfWeek dayOfWeek,
            @RequestParam(defaultValue = "8") int hour
    ) {
        return predictTrafficUseCase.handle(new PredictTrafficQuery(segmentId, dayOfWeek, hour));
    }

    @GetMapping("/anomalies")
    public List<TrafficAnomaly> getAnomalies() {
        return getAnomaliesUseCase.handle();
    }

    @GetMapping("/incidents")
    public List<RoadIncident> getIncidents() {
        return getIncidentsUseCase.handle();
    }

    @GetMapping("/city-stats")
    public CityStats getCityStats() {
        return getCityStatsUseCase.handle();
    }

    @GetMapping("/weather")
    public WeatherSnapshot getWeather(
            @RequestParam(defaultValue = "Baku") String locationName,
            @RequestParam(defaultValue = "40.4093") double latitude,
            @RequestParam(defaultValue = "49.8671") double longitude
    ) {
        return getWeatherUseCase.handle(new WeatherQuery(locationName, latitude, longitude));
    }

    @GetMapping("/smart-eta")
    public SmartEtaResult calculateSmartEta(@RequestParam List<UUID> segmentIds) {
        return calculateSmartEtaUseCase.handle(segmentIds);
    }

    @PostMapping("/report")
    public ResponseEntity<UserReport> submitReport(@Valid @RequestBody SubmitReportRequest request) {
        UserReport report = submitReportUseCase.handle(new SubmitReportCommand(
                request.userId(),
                request.segmentId(),
                request.type(),
                request.description(),
                request.createdAt()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    public record GpsPingRequest(
            @NotBlank String deviceId,
            double latitude,
            double longitude,
            @NotNull Instant timestamp,
            double speedKmh
    ) {
    }

    public record SubmitReportRequest(
            @NotNull UUID userId,
            @NotNull UUID segmentId,
            @NotNull ReportType type,
            @NotBlank String description,
            @NotNull Instant createdAt
    ) {
    }

    public record GpsPingReceiptResponse(GpsPing ping, org.example.waygo.domain.model.RoadSegment segment,
                                         org.example.waygo.domain.model.TrafficSnapshot snapshot,
                                         org.example.waygo.domain.model.TrafficAnomaly anomaly) {
        public static GpsPingReceiptResponse from(org.example.waygo.application.port.in.GpsPingReceipt receipt) {
            return new GpsPingReceiptResponse(receipt.ping(), receipt.segment(), receipt.snapshot(), receipt.anomaly());
        }
    }
}
