package com.pulse.test.controller;

import com.pulse.test.model.TestResult;
import com.pulse.test.service.TestResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/runs")
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;

    @PostMapping
    public ResponseEntity<Void> saveRuns(@RequestBody List<TestResult> results) {
        testResultService.saveAll(results);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<List<TestResult>> getByTicketId(@PathVariable String ticketId) {
        List<TestResult> results = testResultService.getByTicketId(ticketId);
        return ResponseEntity.ok(results);
    }
}