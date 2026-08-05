package com.waygo.api;

import com.waygo.application.port.in.*;
import com.waygo.domain.model.*;
import com.waygo.infrastructure.external.tomtom.TomTomMapsGateway;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final TomTomMapsGateway tomTomMapsGateway;

    public TrafficController(
            ReceiveGpsPingUseCase receiveGpsPingUseCase,
            GetTrafficMapUseCase getTrafficMapUseCase,
            PredictTrafficUseCase predictTrafficUseCase,
            GetAnomaliesUseCase getAnomaliesUseCase,
            SubmitReportUseCase submitReportUseCase,
            GetIncidentsUseCase getIncidentsUseCase,
            GetCityStatsUseCase getCityStatsUseCase,
            GetWeatherUseCase getWeatherUseCase,
            CalculateSmartEtaUseCase calculateSmartEtaUseCase,
            TomTomMapsGateway tomTomMapsGateway
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
        this.tomTomMapsGateway = tomTomMapsGateway;
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
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam int hourOfDay
    ) {
        return predictTrafficUseCase.handle(new PredictTrafficQuery(segmentId, dayOfWeek, hourOfDay));
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
            @RequestParam(defaultValue = "40.4093") double latitude,
            @RequestParam(defaultValue = "49.8671") double longitude
    ) {
        return getWeatherUseCase.handle(new WeatherQuery("Baku", latitude, longitude));
    }

    @PostMapping("/smart-eta")
    public SmartEtaResult calculateSmartEta(@RequestBody List<RoadSegment> segments) {
        List<UUID> segmentIds = segments.stream().map(RoadSegment::id).toList();
        return calculateSmartEtaUseCase.handle(segmentIds);
    }

    @GetMapping("/telemetry/status")
    public TelemetryStatusResponse getHealthTelemetry() {
        long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        return new TelemetryStatusResponse(
                "UP",
                "WayGo Baku Smart Mobility Engine v2.5",
                Instant.now(),
                getAnomaliesUseCase.handle().size(),
                freeMem + "MB / " + maxMem + "MB",
                "Asia/Baku"
        );
    }

    @GetMapping("/map-config")
    public MapConfigResponse getMapConfig() {
        return new MapConfigResponse(
                40.4093,
                49.8671,
                13,
                "https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png",
                "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
                List.of("street", "satellite", "traffic")
        );
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchGeocoding(@RequestParam String q) {
        String resultJson = tomTomMapsGateway.geocodeSearch(q);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(resultJson);
    }

    @GetMapping("/route")
    public ResponseEntity<String> getBackendRoute(
            @RequestParam double fromLat,
            @RequestParam double fromLng,
            @RequestParam double toLat,
            @RequestParam double toLng,
            @RequestParam(defaultValue = "fastest") String mode
    ) {
        String resultJson = tomTomMapsGateway.calculateDirections(fromLat, fromLng, toLat, toLng, mode);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(resultJson);
    }

    @GetMapping(value = "/map/tiles/basic/{z}/{x}/{y}.png", produces = "image/png")
    public ResponseEntity<byte[]> getBasicTile(@PathVariable int z, @PathVariable int x, @PathVariable int y) {
        byte[] tileBytes = tomTomMapsGateway.proxyBasicTile(z, x, y);
        return ResponseEntity.ok().header("Content-Type", "image/png").body(tileBytes);
    }

    @GetMapping(value = "/traffic/tiles/flow/{z}/{x}/{y}.png", produces = "image/png")
    public ResponseEntity<byte[]> getTrafficFlowTile(@PathVariable int z, @PathVariable int x, @PathVariable int y) {
        byte[] tileBytes = tomTomMapsGateway.proxyTrafficFlowTile(z, x, y);
        return ResponseEntity.ok().header("Content-Type", "image/png").body(tileBytes);
    }

    @GetMapping(value = "/traffic/tiles/incidents/{z}/{x}/{y}.png", produces = "image/png")
    public ResponseEntity<byte[]> getTrafficIncidentTile(@PathVariable int z, @PathVariable int x, @PathVariable int y) {
        byte[] tileBytes = tomTomMapsGateway.proxyTrafficIncidentTile(z, x, y);
        return ResponseEntity.ok().header("Content-Type", "image/png").body(tileBytes);
    }

    @GetMapping("/traffic/incidents/realtime")
    public ResponseEntity<String> getRealTimeTomTomIncidents(
            @RequestParam(defaultValue = "49.7") double minLng,
            @RequestParam(defaultValue = "40.3") double minLat,
            @RequestParam(defaultValue = "50.1") double maxLng,
            @RequestParam(defaultValue = "40.5") double maxLat
    ) {
        String incidentsJson = tomTomMapsGateway.fetchRealIncidents(minLng, minLat, maxLng, maxLat);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(incidentsJson);
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

    public record MapConfigResponse(
            double centerLat,
            double centerLng,
            int defaultZoom,
            String streetTileUrl,
            String satelliteTileUrl,
            List<String> availableLayers
    ) {}

    public record TelemetryStatusResponse(
            String status,
            String engineVersion,
            Instant timestamp,
            int activeAnomaliesCount,
            String jvmMemoryUsage,
            String timezone
    ) {}

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

    public record GpsPingReceiptResponse(GpsPing ping, RoadSegment segment,
                                         TrafficSnapshot snapshot,
                                         TrafficAnomaly anomaly) {
        public static GpsPingReceiptResponse from(GpsPingReceipt receipt) {
            return new GpsPingReceiptResponse(receipt.ping(), receipt.segment(), receipt.snapshot(), receipt.anomaly());
        }
    }
}
