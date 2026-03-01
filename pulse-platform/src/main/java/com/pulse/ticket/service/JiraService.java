package com.pulse.ticket.service;

import com.pulse.common.config.JiraProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final JiraProperties jiraProperties;

    /**
     * Builds the Basic Auth header value using email and API token.
     *
     * @return Base64 encoded Basic Auth header value
     */
    private String buildAuthHeader() {
        String credentials = jiraProperties.getEmail() + ":" + jiraProperties.getApiToken();
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    /**
     * Fetches a Jira ticket by its ID and returns the raw response as a Map.
     * Throws an exception if the ticket is not found or the request fails.
     *
     * @param ticketId the Jira ticket ID e.g. "OEMS-1234"
     * @return map containing the raw Jira API response
     */
    public Map<String, Object> getTicket(String ticketId) {
        RestClient restClient = RestClient.create();

        return restClient.get()
                .uri(jiraProperties.getBaseUrl() + "/rest/api/3/issue/" + ticketId)
                .header("Authorization", buildAuthHeader())
                .header("Accept", "application/json")
                .retrieve()
                .body(Map.class);
    }
}