package com.pulse.ai.controller;

import com.pulse.ai.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    /**
     * Accepts a Jira ticket description and PR diff, and returns
     * AI-suggested report and action tags for regression filtering.
     * Requires a valid JWT token.
     *
     * @param body map containing "ticketDescription" and "prDiff" fields
     * @return map containing suggested reportTags and actionTags
     */
    @PostMapping("/suggest-tags")
    public ResponseEntity<Map<String, Object>> suggestTags(@RequestBody Map<String, String> body) {
        String ticketDescription = body.get("ticketDescription");
        String prDiff = body.get("prDiff");

        if (ticketDescription == null || ticketDescription.isBlank()) {
            throw new IllegalArgumentException("The ticket description is required");
        }

        Map<String, Object> suggestions = aiService.suggestTags(ticketDescription, prDiff);
        return ResponseEntity.ok(suggestions);
    }
}