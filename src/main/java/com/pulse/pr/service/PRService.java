package com.pulse.pr.service;

import com.pulse.common.config.GithubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PRService {

    private final GithubProperties githubProperties;

    /**
     * Fetches all open pull requests from the configured GitHub repository.
     * Filters results to only return PRs whose title or branch contains the ticket ID.
     *
     * @param ticketId the Jira ticket ID to filter PRs by e.g. "OEMS-1234"
     * @return list of maps containing PR details linked to the ticket
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPRsForTicket(String ticketId) {
        RestClient restClient = RestClient.create();

        List<Map<String, Object>> allPRs = restClient.get()
                .uri("https://api.github.com/repos/" + githubProperties.getOwner()
                        + "/" + githubProperties.getRepo() + "/pulls?state=open")
                .header("Authorization", "Bearer " + githubProperties.getToken())
                .header("Accept", "application/vnd.github+json")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        if (allPRs == null) return List.of();

        return allPRs.stream()
                .filter(pr -> {
                    String title = (String) pr.getOrDefault("title", "");
                    Map<String, Object> head = (Map<String, Object>) pr.get("head");
                    String branch = head != null ? (String) head.getOrDefault("ref", "") : "";
                    return title.contains(ticketId) || branch.contains(ticketId);
                })
                .map(pr -> {
                    Map<String, Object> head = (Map<String, Object>) pr.get("head");
                    return Map.of(
                        "number", pr.get("number"),
                        "title", pr.getOrDefault("title", ""),
                        "branch", head != null ? head.getOrDefault("ref", "") : "",
                        "url", pr.getOrDefault("html_url", ""),
                        "state", pr.getOrDefault("state", "")
                    );
                })
                .toList();
    }
}