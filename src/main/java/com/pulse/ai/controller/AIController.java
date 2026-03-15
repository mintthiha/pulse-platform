package com.pulse.ai.controller;

import com.pulse.ai.dto.AIRequest;
import com.pulse.ai.dto.AIResponse;
import com.pulse.ai.service.AIService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<AIResponse> suggestTags(@Valid @RequestBody AIRequest request) {
        Map<String, Object> suggestions = aiService.suggestTags(
                request.getTicketDescription(),
                request.getPrDiff()
        );

        @SuppressWarnings("unchecked")
        List<String> reportTags = (List<String>) suggestions.get("reportTags");
        @SuppressWarnings("unchecked")
        List<String> actionTags = (List<String>) suggestions.get("actionTags");

        return ResponseEntity.ok(new AIResponse(reportTags, actionTags));
    }
}