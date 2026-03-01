package com.pulse.pr.controller;

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
     * @param ticketId the Jira ticket ID e.g. "OEMS-1234"
     * @return list of pull requests linked to the ticket
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<List<Map<String, Object>>> getPRsForTicket(@PathVariable String ticketId) {
        List<Map<String, Object>> prs = prService.getPRsForTicket(ticketId);
        return ResponseEntity.ok(prs);
    }
}