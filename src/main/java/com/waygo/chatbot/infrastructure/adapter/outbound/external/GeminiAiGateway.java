package com.waygo.chatbot.infrastructure.adapter.outbound.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public GeminiAiGateway(@Value("${gemini.api-key:}") String apiKey, ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(6))
                .build();
    }

    public String generateAiResponse(String promptWithContext) {
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=" + apiKey;

            // Escaping prompt JSON safely
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
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode textNode = rootNode.at("/candidates/0/content/parts/0/text");
                if (!textNode.isMissingNode()) {
                    return textNode.asText().trim();
                }
            } else {
                System.err.println("[GeminiAiGateway] Error: HTTP " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("[GeminiAiGateway] AI REST API Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
