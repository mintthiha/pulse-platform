package com.pulse.build.service;

import com.pulse.common.config.GithubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BuildService {

    private final GithubProperties githubProperties;
    private final RestClient restClient;

    public void triggerBuild(String branch, String tests) {
        if (branch == null || branch.isBlank()) {
            throw new IllegalArgumentException("Branch must not be null or empty");
        }

        restClient.post()
                .uri("https://api.github.com/repos/" + githubProperties.getOwner()
                        + "/" + githubProperties.getRepo()
                        + "/actions/workflows/regression-run.yml/dispatches")
                .header("Authorization", "Bearer " + githubProperties.getToken())
                .header("Accept", "application/vnd.github+json")
                .body(Map.of(
                        "ref", "main",
                        "inputs", Map.of(
                                "branch", branch,
                                "tests", tests != null ? tests : ""
                        )
                ))
                .retrieve()
                .toBodilessEntity();
    }
}