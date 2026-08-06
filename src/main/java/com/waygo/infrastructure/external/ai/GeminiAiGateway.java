package com.waygo.infrastructure.external.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class GeminiAiGateway {

    private final String apiKey;
    private final HttpClient httpClient;

    public GeminiAiGateway(@Value("${GEMINI_API_KEY:AIzaSyB-DEFAULT-WAYGO-KEY}") String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(6))
                .build();
    }

    public String generateAiResponse(String promptWithContext) {
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

            // Escaping prompt JSON
            String escapedPrompt = promptWithContext
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", " ")
                    .replace("\r", " ");

            String jsonPayload = """
                {
                  "contents": [{
                    "parts": [{"text": "%s"}]
                  }],
                  "generationConfig": {
                    "temperature": 0.4,
                    "maxOutputTokens": 300
                  }
                }
                """.formatted(escapedPrompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .timeout(Duration.ofSeconds(8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 && response.body() != null) {
                String body = response.body();
                int textIdx = body.indexOf("\"text\": \"");
                if (textIdx != -1) {
                    int start = textIdx + 9;
                    int end = body.indexOf("\"", start);
                    if (end != -1) {
                        String rawText = body.substring(start, end);
                        return rawText.replace("\\n", "\n").replace("\\\"", "\"").trim();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[GeminiAiGateway] AI REST API Exception: " + e.getMessage());
        }
        return null;
    }
}
