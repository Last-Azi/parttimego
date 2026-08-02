package com.example.demo.service.ai;

import com.example.demo.config.AiResumeProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiResumeParseClient {

    private static final List<String> FIELDS = List.of(
            "realName",
            "gender",
            "school",
            "major",
            "grade",
            "phone",
            "email",
            "skills",
            "experience",
            "selfIntro",
            "projectExperience",
            "expectCity",
            "expectSalary"
    );

    private final AiResumeProperties properties;
    private final ObjectMapper objectMapper;

    public Map<String, String> parse(String resumeText) {
        if (!properties.isEnabled() || !StringUtils.hasText(properties.getApiKey())
                || !StringUtils.hasText(resumeText)) {
            return Map.of();
        }

        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            Duration timeout = Duration.ofSeconds(properties.getTimeoutSeconds());
            requestFactory.setConnectTimeout(timeout);
            requestFactory.setReadTimeout(timeout);

            RestClient client = RestClient.builder()
                    .baseUrl(trimTrailingSlash(properties.getBaseUrl()))
                    .requestFactory(requestFactory)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            String responseBody = client.post()
                    .uri("/chat/completions")
                    .body(buildRequest(resumeText))
                    .retrieve()
                    .body(String.class);

            String content = objectMapper.readTree(responseBody)
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("");
            return parseStructuredContent(content);
        } catch (Exception e) {
            log.warn("AI resume parse failed, fallback to rule parser: {}", e.getMessage());
            return Map.of();
        }
    }

    Map<String, String> parseStructuredContent(String content) {
        if (!StringUtils.hasText(content)) {
            return Map.of();
        }
        try {
            JsonNode root = objectMapper.readTree(extractJsonObject(content));
            Map<String, String> result = new HashMap<>();
            for (String field : FIELDS) {
                String value = normalize(root.path(field).asText(""));
                if (StringUtils.hasText(value)) {
                    result.put(field, value);
                }
            }
            if (!result.isEmpty()) {
                result.put("parseMode", "ai");
            }
            return result;
        } catch (Exception e) {
            log.warn("AI resume parse content is not valid JSON: {}", e.getMessage());
            return Map.of();
        }
    }

    private Map<String, Object> buildRequest(String resumeText) {
        String clippedText = resumeText;
        if (clippedText.length() > properties.getMaxChars()) {
            clippedText = clippedText.substring(0, properties.getMaxChars());
        }

        String systemPrompt = """
                You are a resume information extraction engine for a Chinese recruiting system.
                Return only one JSON object. Do not include markdown.
                Extract these string fields when present:
                realName, gender, school, major, grade, phone, email, skills,
                experience, selfIntro, projectExperience, expectCity, expectSalary.
                Use comma-separated technical keywords for skills.
                Use empty strings for missing fields.
                """;

        String userPrompt = "Extract structured resume fields from this resume text:\n" + clippedText;

        return Map.of(
                "model", properties.getModel(),
                "temperature", 0.1,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );
    }

    private String extractJsonObject(String content) {
        String trimmed = content.trim();
        int start = trimmed.indexOf('{');
        int end = trimmed.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return trimmed.substring(start, end + 1);
        }
        return trimmed;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.trim()
                .replace("\r\n", "\n")
                .replace('\r', '\n')
                .replaceAll("\\n{3,}", "\n\n");
        if ("null".equalsIgnoreCase(normalized) || "unknown".equalsIgnoreCase(normalized)) {
            return "";
        }
        return normalized.length() > 800 ? normalized.substring(0, 800) + "..." : normalized;
    }

    private String trimTrailingSlash(String value) {
        if (!StringUtils.hasText(value)) {
            return "https://api.openai.com/v1";
        }
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }
}
