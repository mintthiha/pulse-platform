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

    /**
     * Triggers the regression workflow on the specified branch in the testing repo.
     * Passes the branch and comma-separated test list as workflow inputs.
     *
     * @param branch the branch to run the workflow on
     * @param tests  comma-separated list of test scenario names to run
     */
    public void triggerBuild(String branch, String tests) {
        RestClient restClient = RestClient.create();

        restClient.post()
                .uri("https://api.github.com/repos/" + githubProperties.getOwner()
                        + "/" + githubProperties.getRepo()
                        + "/actions/workflows/regression-run.yml/dispatches")
                .header("Authorization", "Bearer " + githubProperties.getToken())
                .header("Accept", "application/vnd.github+json")
                .body(Map.of(
                        "ref", branch,
                        "inputs", Map.of(
                                "branch", branch,
                                "tests", tests != null ? tests : ""
                        )
                ))
                .retrieve()
                .toBodilessEntity();
    }
}