package com.pulse.ticket.controller;

import com.pulse.ticket.service.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final JiraService jiraService;

    /**
     * Fetches a Jira ticket by its ID and returns key fields.
     * Requires a valid JWT token.
     *
     * @param ticketId the Jira ticket ID e.g. "OEMS-1234"
     * @return map containing ticket summary, description and status
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<Map<String, Object>> getTicket(@PathVariable String ticketId) {
        Map<String, Object> raw = jiraService.getTicket(ticketId);

        Map<String, Object> fields = (Map<String, Object>) raw.get("fields");

        Map<String, Object> response = Map.of(
            "ticketId", ticketId,
            "summary", fields.getOrDefault("summary", ""),
            "status", ((Map<String, Object>) fields.get("status")).get("name"),
            "description", fields.getOrDefault("description", "")
        );

        return ResponseEntity.ok(response);
    }
}