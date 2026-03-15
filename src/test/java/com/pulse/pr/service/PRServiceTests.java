package com.pulse.pr.service;

import com.pulse.common.config.GithubProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PRServiceTests {

    @Mock
    private GithubProperties githubProperties;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private PRService prService;

    // Should return only PRs whose title or branch contains the ticket ID
    @Test
    void getPRsForTicket_returnsFilteredPRs() {
        Map<String, Object> matchingPR = Map.of(
                "number", 1,
                "title", "SCRUM-5 - Test Equity Order cancellation",
                "head", Map.of("ref", "SCRUM-5-test-equity-order-cancellation"),
                "html_url", "https://github.com/mintthiha/pulse-demo-tests/pull/1",
                "state", "open"
        );
        Map<String, Object> nonMatchingPR = Map.of(
                "number", 2,
                "title", "SCRUM-9 - Some other ticket",
                "head", Map.of("ref", "SCRUM-9-some-other-branch"),
                "html_url", "https://github.com/mintthiha/pulse-demo-tests/pull/2",
                "state", "open"
        );

        when(githubProperties.getOwner()).thenReturn("mintthiha");
        when(githubProperties.getRepo()).thenReturn("pulse-demo-tests");
        when(githubProperties.getToken()).thenReturn("gh_token");
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(requestHeadersSpec).when(requestHeadersSpec).header(anyString(), anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(List.of(matchingPR, nonMatchingPR)).when(responseSpec).body(any(ParameterizedTypeReference.class));

        List<Map<String, Object>> results = prService.getPRsForTicket("SCRUM-5");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).get("title")).isEqualTo("SCRUM-5 - Test Equity Order cancellation");
    }

    // Should return empty list when GitHub returns null
    @Test
    void getPRsForTicket_returnsEmptyListWhenNullResponse() {
        when(githubProperties.getOwner()).thenReturn("mintthiha");
        when(githubProperties.getRepo()).thenReturn("pulse-demo-tests");
        when(githubProperties.getToken()).thenReturn("gh_token");
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(requestHeadersSpec).when(requestHeadersSpec).header(anyString(), anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(null).when(responseSpec).body(any(ParameterizedTypeReference.class));

        List<Map<String, Object>> results = prService.getPRsForTicket("SCRUM-5");

        assertThat(results).isEmpty();
    }

    // Should return empty list when no PRs match the ticket ID
    @Test
    void getPRsForTicket_returnsEmptyListWhenNoMatch() {
        Map<String, Object> nonMatchingPR = Map.of(
                "number", 1,
                "title", "SCRUM-9 - Some other ticket",
                "head", Map.of("ref", "SCRUM-9-some-other-branch"),
                "html_url", "https://github.com/mintthiha/pulse-demo-tests/pull/1",
                "state", "open"
        );

        when(githubProperties.getOwner()).thenReturn("mintthiha");
        when(githubProperties.getRepo()).thenReturn("pulse-demo-tests");
        when(githubProperties.getToken()).thenReturn("gh_token");
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(requestHeadersSpec).when(requestHeadersSpec).header(anyString(), anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(List.of(nonMatchingPR)).when(responseSpec).body(any(ParameterizedTypeReference.class));

        List<Map<String, Object>> results = prService.getPRsForTicket("SCRUM-5");

        assertThat(results).isEmpty();
    }
}