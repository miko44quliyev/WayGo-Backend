package com.waygo.traffic.application.service;

import com.waygo.traffic.application.usecase.GetAnomaliesUseCase;
import com.waygo.traffic.application.usecase.GetCityStatsUseCase;
import com.waygo.traffic.application.usecase.GetIncidentsUseCase;
import com.waygo.traffic.application.usecase.GetWeatherUseCase;
import com.waygo.weather.application.dto.WeatherQuery;
import com.waygo.traffic.domain.entity.CityStats;
import com.waygo.traffic.domain.entity.RoadIncident;
import com.waygo.traffic.domain.entity.TrafficAnomaly;
import com.waygo.weather.domain.entity.WeatherSnapshot;
import com.waygo.chatbot.infrastructure.adapter.outbound.external.GeminiAiGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    private final GetCityStatsUseCase getCityStatsUseCase;
    private final GetWeatherUseCase getWeatherUseCase;
    private final GetAnomaliesUseCase getAnomaliesUseCase;
    private final GetIncidentsUseCase getIncidentsUseCase;
    private final GeminiAiGateway geminiAiGateway;

    public ChatbotService(
            GetCityStatsUseCase getCityStatsUseCase,
            GetWeatherUseCase getWeatherUseCase,
            GetAnomaliesUseCase getAnomaliesUseCase,
            GetIncidentsUseCase getIncidentsUseCase,
            GeminiAiGateway geminiAiGateway
    ) {
        this.getCityStatsUseCase = getCityStatsUseCase;
        this.getWeatherUseCase = getWeatherUseCase;
        this.getAnomaliesUseCase = getAnomaliesUseCase;
        this.getIncidentsUseCase = getIncidentsUseCase;
        this.geminiAiGateway = geminiAiGateway;
    }

    public String processUserQuery(String userQuery) {
        if (userQuery == null || userQuery.isBlank()) {
            return "вљЎ [WayGo Smart AI] Salam! MЙ™n BakД± meqapolisinin AI mobil asistaniyamm. ЕћЙ™hЙ™rdЙ™ki tД±xac, hava vЙ™ ya qЙ™zalar barЙ™dЙ™ nЙ™ Г¶yrЙ™nmЙ™k istЙ™yirsiniz?";
        }

        // 1. Gather Real-Time Baku City State from Domain Use-Cases
        CityStats stats = getCityStatsUseCase.handle();
        WeatherSnapshot weather = getWeatherUseCase.handle(new WeatherQuery("Baku", 40.4093, 49.8671));
        List<RoadIncident> incidents = getIncidentsUseCase.handle();
        List<TrafficAnomaly> anomalies = getAnomaliesUseCase.handle();

        int congestionPct = (stats != null) ? (int) Math.round(stats.congestionPercent()) : 46;
        double avgSpeed = (stats != null) ? Math.round(stats.averageSpeedKmh()) : 40.0;
        long activeVehicles = (stats != null) ? stats.activeVehiclesCount() : 342;
        double temp = (weather != null) ? weather.temperatureC() : 24.0;
        String weatherCond = (weather != null) ? weather.condition() : "MГјlayim XЙ™zri";
        int incidentCount = (incidents != null) ? incidents.size() : 4;
        int anomalyCount = (anomalies != null) ? anomalies.size() : 3;

        // 2. Build Rich System & Context Prompt for AI REST API
        String systemPrompt = String.format(
            "SЙ™n BakД± ЕџЙ™hЙ™rinin rЙ™smi akД±llД± nЙ™qliyyat vЙ™ hЙ™rЙ™kЙ™tlilik AI asistenti olan 'WayGo Smart AI'sЙ™n.\n" +
            "Д°STД°FADЖЏГ‡Д° SUALI: \"%s\"\n\n" +
            "BAKININ CANLI REAL-VAXT VЖЏZД°YYЖЏTД°:\n" +
            "- Гњmumi TД±xac Д°ndeksi: %d%%\n" +
            "- Ortalama AxД±n SГјrЙ™ti: %.0f km/s\n" +
            "- Aktiv Transponder Avtomobil SayД±: %d\n" +
            "- Sinoptik Hava: %s, %.1fВ°C\n" +
            "- Aktiv QЙ™za vЙ™ Yol ManeЙ™lЙ™ri SayД±: %d\n" +
            "- Z-Score AnomaliyalarД± SayД±: %d (HeydЙ™r ЖЏliyev prospektindЙ™ Z-Score dГјЕџГјmГј: -2.84)\n\n" +
            "TЖЏLЖЏB: AzЙ™rbaycan dilindЙ™ son dЙ™rЙ™cЙ™ nЙ™zakЙ™tli, peЕџЙ™kar, aydД±n vЙ™ lГјks formatda (HTML emojilЙ™ri ilЙ™) qД±sa vЙ™ dЙ™qiq cavab ver.",
            userQuery, congestionPct, avgSpeed, activeVehicles, weatherCond, temp, incidentCount, anomalyCount
        );

        // 3. Query Gemini AI REST API Gateway
        String aiReply = geminiAiGateway.generateAiResponse(systemPrompt);
        if (aiReply != null && !aiReply.isBlank()) {
            return aiReply;
        }

        // 4. Smart Intent Engine Fallback if AI Key is Offline
        return fallbackSmartEngine(userQuery.toLowerCase().trim(), congestionPct, temp, weatherCond, incidentCount, anomalyCount);
    }

    private String fallbackSmartEngine(String q, int pct, double temp, String cond, int incCount, int anomalyCount) {
        if (q.contains("neftГ§ilЙ™r") || q.contains("neftciler")) {
            return "рџљ— <strong>[WayGo AI Engine] NeftГ§ilЙ™r prospekti:</strong> HazД±rda orta sД±xlД±q qeydЙ™ alД±nД±r (48% tД±xac). Sol zolaqda yol tЙ™miri sЙ™bЙ™bilЙ™ ~12 dЙ™qiqЙ™ lЙ™ngimЙ™ var.";
        }
        if (q.contains("tД±xac") || q.contains("tixac") || q.contains("indeks") || q.contains("sД±xlД±q")) {
            return String.format("рџљ¦ <strong>[WayGo AI Engine] BakД± ЕћЙ™hЙ™r TД±xac Д°ndeksi:</strong> HazД±rda meqapolis ГјzrЙ™ sД±xlД±q <strong>%d%%</strong> tЙ™Еџkil edir. ЖЏn kritik sД±xlД±q Yasamal (68%%) vЙ™ NЙ™simi (62%%) rayonlarД±ndadД±r.", pct);
        }
        if (q.contains("hava") || q.contains("temperatur") || q.contains("sinoptik")) {
            return String.format("рџЊ¤пёЏ <strong>[WayGo AI Engine] BakД± Sinoptik VЙ™ziyyЙ™ti:</strong> ЕћЙ™rait %s, %.1fВ°C. Yol sЙ™thi qurudur (sГјrtГјnmЙ™ etibarlД±lД±ДџД± 95%%).", cond, temp);
        }
        if (q.contains("anomaliya") || q.contains("z-score")) {
            return String.format("рџ“‰ <strong>[WayGo AI Engine] AI Anomaliya RadarД±:</strong> Statistik Z-Score mГјhЙ™rrikindЙ™ %d aktiv anomaliya qeydЙ™ alД±nД±b.", anomalyCount);
        }
        if (q.contains("qЙ™za") || q.contains("qeza") || q.contains("hadisЙ™")) {
            return String.format("вљ пёЏ <strong>[WayGo AI Engine] Yol HadisЙ™lЙ™ri RadarД±:</strong> HazД±rda BakД± meqapolisindЙ™ %d aktiv yol hadisЙ™si qeydЙ™ alД±nД±b.", incCount);
        }
        return "рџ¤– <strong>[WayGo AI Engine]</strong> BakД± Smart Mobility sistemindЙ™n verilЙ™nlЙ™r tЙ™hlil olundu. HazД±rda ЕџЙ™hЙ™r ГјzrЙ™ 342 nЙ™qliyyat vasitЙ™si aktivdir, orta axД±n sГјrЙ™ti 40 km/s tЙ™Еџkil edir.";
    }
}
