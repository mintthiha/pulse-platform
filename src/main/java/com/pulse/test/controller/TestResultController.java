package com.pulse.test.controller;

import com.pulse.test.dto.TestResultRequest;
import com.pulse.test.dto.TestResultResponse;
import com.pulse.test.model.TestResult;
import com.pulse.test.service.TestResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/runs")
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

    /**
     * Accepts a list of test results from CI/CD and persists them to the database.
     * This endpoint is public and used by GitHub Actions after a nightly run.
     *
     * @param requests list of validated test result request DTOs
     * @return 200 OK when all results are saved successfully
     */
    @PostMapping
    public ResponseEntity<Void> saveRuns(@Valid @RequestBody List<TestResultRequest> requests) {
        List<TestResult> results = requests.stream().map(r -> {
            TestResult result = new TestResult();
            result.setScenarioName(r.getScenarioName());
            result.setTicketId(r.getTicketId());
            result.setStatus(r.getStatus());
            result.setBranch(r.getBranch());
            result.setRunTimestamp(r.getRunTimestamp());
            result.setDurationMs(r.getDurationMs());
            result.setReportTags(r.getReportTags());
            result.setActionTags(r.getActionTags());
            return result;
        }).toList();

        testResultService.saveAll(results);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns all test results associated with the given Jira ticket ID.
     * Requires a valid JWT token.
     *
     * @param ticketId the Jira ticket ID to filter results by
     * @return list of test result response DTOs matching the ticket ID
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<List<TestResultResponse>> getByTicketId(@PathVariable String ticketId) {
        List<TestResultResponse> responses = testResultService.getByTicketId(ticketId)
                .stream()
                .map(r -> new TestResultResponse(
                        r.getId(),
                        r.getScenarioName(),
                        r.getTicketId(),
                        r.getStatus(),
                        r.getBranch(),
                        r.getRunTimestamp(),
                        r.getDurationMs(),
                        r.getReportTags(),
                        r.getActionTags()
                ))
                .toList();

        return ResponseEntity.ok(responses);
    }
}