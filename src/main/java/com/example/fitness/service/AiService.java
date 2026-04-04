package com.example.fitness.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder().build();

    public String generateRecommendation(String prompt) {

        Map<String, Object> request = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        return webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> candidates =
                            (List<Map<String, Object>>) response.get("candidates");

                    Map<String, Object> content =
                            (Map<String, Object>) candidates.get(0).get("content");

                    List<Map<String, Object>> parts =
                            (List<Map<String, Object>>) content.get("parts");

                    return parts.get(0).get("text").toString();
                })
                .block();
    }
}
