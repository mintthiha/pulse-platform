package com.pulse.build.controller;

import com.pulse.build.dto.BuildRequest;
import com.pulse.build.service.BuildService;

import jakarta.validation.Valid;
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
    public ResponseEntity<String> triggerBuild(@Valid @RequestBody BuildRequest request) {
        buildService.triggerBuild(request.getBranch(), request.getTests());
        return ResponseEntity.ok("Build triggered successfully");
    }
}