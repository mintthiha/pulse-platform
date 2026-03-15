package com.pulse.pr.controller;

import com.pulse.pr.dto.PRResponse;
import com.pulse.pr.service.PRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prs")
@RequiredArgsConstructor
public class PRController {

    private final PRService prService;

    /**
     * Fetches all open GitHub pull requests linked to the given Jira ticket ID.
     * Requires a valid JWT token.
     *
     * @param ticketId the Jira ticket ID e.g. "SCRUM-5"
     * @return list of PR response DTOs linked to the ticket
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<List<PRResponse>> getPRsForTicket(@PathVariable String ticketId) {
        List<Map<String, Object>> prs = prService.getPRsForTicket(ticketId);

        List<PRResponse> responses = prs.stream()
                .map(pr -> new PRResponse(
                        (Integer) pr.get("number"),
                        (String) pr.get("title"),
                        (String) pr.get("branch"),
                        (String) pr.get("url"),
                        (String) pr.get("state")
                ))
                .toList();

        return ResponseEntity.ok(responses);
    }
}