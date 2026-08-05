package waygo.presentation.rest;

import waygo.application.port.in.*;
import waygo.domain.model.*;
import waygo.infrastructure.google.GoogleMapsGateway;
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
    private final GoogleMapsGateway googleMapsGateway;

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
            GoogleMapsGateway googleMapsGateway
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
        this.googleMapsGateway = googleMapsGateway;
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

    @PostMapping("/incidents")
    public ResponseEntity<UserReport> submitIncident(@Valid @RequestBody SubmitReportRequest request) {
        UserReport report = submitReportUseCase.handle(new SubmitReportCommand(
                request.userId(),
                request.segmentId(),
                request.type(),
                request.description(),
                request.createdAt()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
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

    @GetMapping("/health-telemetry")
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

    // 🌐 BACKEND MAP CONFIGURATION API
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

    // 🔍 BACKEND GEOCODING PROXY API (Google Maps / OSM Gateway)
    @GetMapping("/search")
    public ResponseEntity<String> searchGeocoding(@RequestParam String q) {
        String resultJson = googleMapsGateway.geocodeSearch(q);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(resultJson);
    }

    // 🚗 BACKEND ROUTING PROXY API (Google Maps Directions / OSRM Gateway)
    @GetMapping("/route")
    public ResponseEntity<String> getBackendRoute(
            @RequestParam double fromLat,
            @RequestParam double fromLng,
            @RequestParam double toLat,
            @RequestParam double toLng,
            @RequestParam(defaultValue = "fastest") String mode
    ) {
        String resultJson = googleMapsGateway.calculateDirections(fromLat, fromLng, toLat, toLng, mode);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(resultJson);
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

    public record GpsPingReceiptResponse(GpsPing ping, RoadSegment segment,
                                         TrafficSnapshot snapshot,
                                         TrafficAnomaly anomaly) {
        public static GpsPingReceiptResponse from(GpsPingReceipt receipt) {
            return new GpsPingReceiptResponse(receipt.ping(), receipt.segment(), receipt.snapshot(), receipt.anomaly());
        }
    }
}
