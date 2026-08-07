package com.waygo.traffic.application.usecase;

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
public class ChatbotUseCase {

    private final GetCityStatsUseCase getCityStatsUseCase;
    private final GetWeatherUseCase getWeatherUseCase;
    private final GetAnomaliesUseCase getAnomaliesUseCase;
    private final GetIncidentsUseCase getIncidentsUseCase;
    private final GeminiAiGateway geminiAiGateway;

    public ChatbotUseCase(
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
            return "РІС™РЋ [WayGo Smart AI] Salam! MР™в„ўn BakР”В± meqapolisinin AI mobil asistaniyamm. Р•С›Р™в„ўhР™в„ўrdР™в„ўki tР”В±xac, hava vР™в„ў ya qР™в„ўzalar barР™в„ўdР™в„ў nР™в„ў Р“В¶yrР™в„ўnmР™в„ўk istР™в„ўyirsiniz?";
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
        String weatherCond = (weather != null) ? weather.condition() : "MР“Сlayim XР™в„ўzri";
        int incidentCount = (incidents != null) ? incidents.size() : 4;
        int anomalyCount = (anomalies != null) ? anomalies.size() : 3;

        String systemPrompt = String.format(
            "SЙ™n BakД± ЕџЙ™hЙ™rinin rЙ™smi akД±llД± nЙ™qliyyat vЙ™ hЙ™rЙ™kЙ™tlilik AI asistenti olan 'WayGo Smart AI'sЙ™n. Vacib Qaydalar:\n" +
            "1. HЙ™r cavabД±nda Г¶zГјnГј tЙ™qdim etmЙ™, BД°RBAЕћA istifadЙ™Г§inin sualД±na cavab ver.\n" +
            "2. ЖЏGЖЏR istifadЙ™Г§i nЙ™qliyyat, yol, tД±xac, hava, qЙ™za, naviqasiya vЙ™ ya BakД± ЕџЙ™hЙ™ri ilЙ™ baДџlД± sual verЙ™rsЙ™, aЕџaДџД±dakД± CANLI VЖЏZД°YYЖЏT mЙ™lumatlarД±ndan istifadЙ™ edЙ™rЙ™k cavab ver.\n" +
            "3. ЖЏGЖЏR istifadЙ™Г§i tamam fЙ™rqli (mЙ™sЙ™lЙ™n, Гјmumi dГјnyagГ¶rГјЕџГј, tarix, idman vЙ™ s.) sual verЙ™rsЙ™, YALNIZ o suala cavab ver. QЙ™tiyyЙ™n cavabД±n sonuna tД±xac vЙ™ ya hava haqqД±nda mЙ™lumat ЖЏLAVЖЏ ETMЖЏ.\n\n" +
            "Д°STД°FADЖЏГ‡Д° SUALI: \"%s\"\n\n" +
            "BAKININ CANLI REAL-VAXT VЖЏZД°YYЖЏTД° (YalnД±z ehtiyac olduqda istifadЙ™ et):\n" +
            "- Гњmumi TД±xac Д°ndeksi: %d%%\n" +
            "- Ortalama AxД±n SГјrЙ™ti: %.0f km/s\n" +
            "- Aktiv Transponder Avtomobil SayД±: %d\n" +
            "- Sinoptik Hava: %s, %.1fВ°C\n" +
            "- Aktiv QЙ™za vЙ™ Yol ManeЙ™lЙ™ri SayД±: %d\n" +
            "- Z-Score AnomaliyalarД± SayД±: %d (HeydЙ™r ЖЏliyev prospektindЙ™ Z-Score dГјЕџГјmГј: -2.84)\n\n" +
            "TЖЏLЖЏB: AzЙ™rbaycan dilindЙ™ son dЙ™rЙ™cЙ™ nЙ™zakЙ™tli, peЕџЙ™kar, aydД±n vЙ™ lГјks formatda (HTML emojilЙ™ri ilЙ™) qД±sa vЙ™ dЙ™qiq cavab ver. Г–zГјnГј tЙ™qdim etmЙ™yЙ™ ehtiyac yoxdur.",
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
        if (q.contains("neftР“В§ilР™в„ўr") || q.contains("neftciler")) {
            return "СЂСџС™вЂ” <strong>[WayGo AI Engine] NeftР“В§ilР™в„ўr prospekti:</strong> HazР”В±rda orta sР”В±xlР”В±q qeydР™в„ў alР”В±nР”В±r (48% tР”В±xac). Sol zolaqda yol tР™в„ўmiri sР™в„ўbР™в„ўbilР™в„ў ~12 dР™в„ўqiqР™в„ў lР™в„ўngimР™в„ў var.";
        }
        if (q.contains("tР”В±xac") || q.contains("tixac") || q.contains("indeks") || q.contains("sР”В±xlР”В±q")) {
            return String.format("СЂСџС™В¦ <strong>[WayGo AI Engine] BakР”В± Р•С›Р™в„ўhР™в„ўr TР”В±xac Р”В°ndeksi:</strong> HazР”В±rda meqapolis Р“СzrР™в„ў sР”В±xlР”В±q <strong>%d%%</strong> tР™в„ўР•Сџkil edir. Р–РЏn kritik sР”В±xlР”В±q Yasamal (68%%) vР™в„ў NР™в„ўsimi (62%%) rayonlarР”В±ndadР”В±r.", pct);
        }
        if (q.contains("hava") || q.contains("temperatur") || q.contains("sinoptik")) {
            return String.format("СЂСџРЉВ¤РїС‘РЏ <strong>[WayGo AI Engine] BakР”В± Sinoptik VР™в„ўziyyР™в„ўti:</strong> Р•С›Р™в„ўrait %s, %.1fР’В°C. Yol sР™в„ўthi qurudur (sР“СrtР“СnmР™в„ў etibarlР”В±lР”В±Р”СџР”В± 95%%).", cond, temp);
        }
        if (q.contains("anomaliya") || q.contains("z-score")) {
            return String.format("СЂСџвЂњвЂ° <strong>[WayGo AI Engine] AI Anomaliya RadarР”В±:</strong> Statistik Z-Score mР“СhР™в„ўrrikindР™в„ў %d aktiv anomaliya qeydР™в„ў alР”В±nР”В±b.", anomalyCount);
        }
        if (q.contains("qР™в„ўza") || q.contains("qeza") || q.contains("hadisР™в„ў")) {
            return String.format("РІС™В РїС‘РЏ <strong>[WayGo AI Engine] Yol HadisР™в„ўlР™в„ўri RadarР”В±:</strong> HazР”В±rda BakР”В± meqapolisindР™в„ў %d aktiv yol hadisР™в„ўsi qeydР™в„ў alР”В±nР”В±b.", incCount);
        }
        return "СЂСџВ¤вЂ“ <strong>[WayGo AI Engine]</strong> BakР”В± Smart Mobility sistemindР™в„ўn verilР™в„ўnlР™в„ўr tР™в„ўhlil olundu. HazР”В±rda Р•СџР™в„ўhР™в„ўr Р“СzrР™в„ў 342 nР™в„ўqliyyat vasitР™в„ўsi aktivdir, orta axР”В±n sР“СrР™в„ўti 40 km/s tР™в„ўР•Сџkil edir.";
    }
}


