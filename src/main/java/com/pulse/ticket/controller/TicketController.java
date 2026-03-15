package com.pulse.ticket.controller;

import com.pulse.ticket.dto.TicketResponse;
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
     * Fetches a Jira ticket by its ID and returns key fields as a response DTO.
     * Requires a valid JWT token.
     *
     * @param ticketId the Jira ticket ID e.g. "SCRUM-5"
     * @return TicketResponse containing ticket summary, status and description
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable String ticketId) {
        Map<String, Object> raw = jiraService.getTicket(ticketId);
        Map<String, Object> fields = (Map<String, Object>) raw.get("fields");
        Map<String, Object> statusMap = (Map<String, Object>) fields.get("status");

        TicketResponse response = new TicketResponse(
                ticketId,
                (String) fields.getOrDefault("summary", ""),
                (String) statusMap.get("name"),
                fields.getOrDefault("description", "")
        );

        return ResponseEntity.ok(response);
    }
}