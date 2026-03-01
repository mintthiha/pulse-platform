package com.pulse.build.controller;

import com.pulse.build.service.BuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/builds")
@RequiredArgsConstructor
public class BuildController {

    private final BuildService buildService;

    /**
     * Triggers a regression build on the specified branch.
     * Requires a valid JWT token.
     *
     * @param body map containing "branch" and optional "tests" fields
     * @return 200 OK when the build is successfully triggered
     */
    @PostMapping("/trigger")
    public ResponseEntity<String> triggerBuild(@RequestBody Map<String, String> body) {
        String branch = body.get("branch");
        String tests = body.get("tests");

        if (branch == null || branch.isBlank()) {
            throw new IllegalArgumentException("Branch is required");
        }

        buildService.triggerBuild(branch, tests);
        return ResponseEntity.ok("Build triggered successfully");
    }
}