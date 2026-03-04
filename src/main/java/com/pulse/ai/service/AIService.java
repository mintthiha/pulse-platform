package com.pulse.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.common.config.OpenAIProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final OpenAIProperties openAIProperties;

    /**
     * Sends the Jira ticket description and PR diff to OpenAI and returns
     * suggested report and action tags for regression filtering.
     *
     * @param ticketDescription the Jira ticket description text
     * @param prDiff            the pull request code diff
     * @return map containing suggested reportTags and actionTags arrays
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> suggestTags(String ticketDescription, String prDiff) {
        RestClient restClient = RestClient.create();

        String prompt = """
                You are a test automation assistant. Based on the following Jira ticket description and pull request diff, suggest relevant report tags and action tags for regression testing.
                
                Report tags represent broad application areas (e.g. PWM, Technology, RiskManagement).
                Action tags represent specific features (e.g. BondOrder, EquityOrder, OrderCancellation).
                
                Jira Ticket Description:
                %s
                
                Pull Request Diff:
                %s
                
                Respond ONLY with a JSON object in this exact format, no explanation:
                {"reportTags": ["tag1", "tag2"], "actionTags": ["tag1", "tag2"]}
                """.formatted(ticketDescription, prDiff);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        Map<String, Object> response = restClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + openAIProperties.getApiKey())
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) message.get("content");

        try {
            // Parse the JSON response from OpenAI
            content = content.trim();
            if (content.startsWith("```")) {
                content = content.replaceAll("```json", "").replaceAll("```", "").trim();
            }

            return new ObjectMapper().readValue(content, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + content, e);
        }
    }
}