package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai.resume")
public class AiResumeProperties {

    private boolean enabled = false;

    private String apiKey = "";

    private String baseUrl = "https://api.openai.com/v1";

    private String model = "gpt-4o-mini";

    private int timeoutSeconds = 20;

    private int maxChars = 6000;
}
