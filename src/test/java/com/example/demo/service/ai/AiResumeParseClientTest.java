package com.example.demo.service.ai;

import com.example.demo.config.AiResumeProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AiResumeParseClientTest {

    private final AiResumeParseClient client =
            new AiResumeParseClient(new AiResumeProperties(), new ObjectMapper());

    @Test
    void parseStructuredContentExtractsKnownResumeFields() {
        String content = """
                {
                  "realName": "Huang Yufeng",
                  "school": "Guangdong University of Finance and Economics",
                  "major": "Software Engineering",
                  "phone": "17819299934",
                  "email": "3094266842@qq.com",
                  "skills": "Java,Spring Boot,MySQL,Redis",
                  "projectExperience": "PartTimeGo recruiting platform"
                }
                """;

        Map<String, String> result = client.parseStructuredContent(content);

        assertThat(result).containsEntry("realName", "Huang Yufeng");
        assertThat(result).containsEntry("skills", "Java,Spring Boot,MySQL,Redis");
        assertThat(result).containsEntry("parseMode", "ai");
    }

    @Test
    void parseStructuredContentIgnoresInvalidContent() {
        assertThat(client.parseStructuredContent("not json")).isEmpty();
    }

    @Test
    void parseReturnsEmptyWhenDisabled() {
        assertThat(client.parse("Java backend resume")).isEmpty();
    }
}
