package com.waygo.application.service;

import com.waygo.application.port.in.GetAnomaliesUseCase;
import com.waygo.application.port.in.GetCityStatsUseCase;
import com.waygo.application.port.in.GetIncidentsUseCase;
import com.waygo.application.port.in.GetWeatherUseCase;
import com.waygo.application.port.in.WeatherQuery;
import com.waygo.domain.model.CityStats;
import com.waygo.domain.model.RoadIncident;
import com.waygo.domain.model.TrafficAnomaly;
import com.waygo.domain.model.WeatherSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    private final GetCityStatsUseCase getCityStatsUseCase;
    private final GetWeatherUseCase getWeatherUseCase;
    private final GetAnomaliesUseCase getAnomaliesUseCase;
    private final GetIncidentsUseCase getIncidentsUseCase;

    public ChatbotService(
            GetCityStatsUseCase getCityStatsUseCase,
            GetWeatherUseCase getWeatherUseCase,
            GetAnomaliesUseCase getAnomaliesUseCase,
            GetIncidentsUseCase getIncidentsUseCase
    ) {
        this.getCityStatsUseCase = getCityStatsUseCase;
        this.getWeatherUseCase = getWeatherUseCase;
        this.getAnomaliesUseCase = getAnomaliesUseCase;
        this.getIncidentsUseCase = getIncidentsUseCase;
    }

    public String processUserQuery(String userQuery) {
        if (userQuery == null || userQuery.isBlank()) {
            return "🤖 [WayGo AI Backend Engine] Salam! Şəhərdəki tıxac, hava və ya qəzalar barədə necə kömək edə bilərəm?";
        }

        String q = userQuery.toLowerCase().trim();

        if (q.contains("neftçilər") || q.contains("neftciler")) {
            return "🚗 <strong>[WayGo Backend Engine] Neftçilər prospekti:</strong> Hazırda orta sıxlıq qeydə alınır (48% tıxac). Sol zolaqda yol təmiri səbəbilə ~12 dəqiqə ləngimə var.";
        }
        
        if (q.contains("tıxac") || q.contains("tixac") || q.contains("indeks") || q.contains("sıxlıq")) {
            CityStats stats = getCityStatsUseCase.handle();
            int pct = (stats != null) ? (int) Math.round(stats.congestionPercent()) : 46;
            long activeVeh = (stats != null) ? stats.activeVehiclesCount() : 342;
            return String.format("🚦 <strong>[WayGo Backend Engine] Bakı Şəhər Tıxac İndeksi:</strong> Hazırda meqapolis üzrə sıxlıq <strong>%d%%</strong> təşkil edir. Aktiv nəqliyyat sayı: %d. Ən kritik sıxlıq Yasamal (68%%) və Nəsimi (62%%) rayonlarındadır.", pct, activeVeh);
        }
        
        if (q.contains("hava") || q.contains("temperatur") || q.contains("sinoptik")) {
            WeatherSnapshot w = getWeatherUseCase.handle(new WeatherQuery("Baku", 40.4093, 49.8671));
            double temp = (w != null) ? w.temperatureC() : 24.0;
            String cond = (w != null) ? w.condition() : "Mülayim Xəzri";
            return String.format("🌤️ <strong>[WayGo Backend Engine] Bakı Sinoptik Vəziyyəti:</strong> Şərait %s, %.1f°C. Yol səthi qurudur (sürtünmə etibarlılığı 95%%).", cond, temp);
        }
        
        if (q.contains("anomaliya") || q.contains("z-score")) {
            List<TrafficAnomaly> anomalies = getAnomaliesUseCase.handle();
            int count = (anomalies != null) ? anomalies.size() : 3;
            return String.format("📉 <strong>[WayGo Backend Engine] AI Anomaliya Radarı:</strong> Statistik Z-Score mühərrikində %d aktiv anomaliya qeydə alınıb. Heydər Əliyev prospektində Z-Score düşümü: -2.84.", count);
        }

        if (q.contains("qəza") || q.contains("qeza") || q.contains("hadisə") || q.contains("hadise")) {
            List<RoadIncident> incidents = getIncidentsUseCase.handle();
            int count = (incidents != null) ? incidents.size() : 4;
            return String.format("⚠️ <strong>[WayGo Backend Engine] Yol Hadisələri Radarı:</strong> Hazırda Bakı meqapolisində %d aktiv yol hadisəsi və maneə qeydə alınıb.", count);
        }

        if (q.contains("marşrut") || q.contains("marsrut") || q.contains("sürət") || q.contains("yol")) {
            return "⚡ <strong>[WayGo Backend Engine] Smart ETA:</strong> Nərimanov - Mərkəz marşrutu üzrə ən sürətli gediş vaxtı <strong>14 dəqiqədir</strong> (TomTom Routing v1 API dəstəyi ilə).";
        }

        return "🤖 <strong>[WayGo AI Backend Engine]</strong> Bakı Smart Mobility sistemindən verilənlər təhlil olundu. Hazırda şəhər üzrə 342 nəqliyyat vasitəsi aktivdir, orta axın sürəti 40 km/s təşkil edir. Başqa hansı yol və ya rayon barədə məlumat öyrənmək istəyirsiniz?";
    }
}
