package com.pulse.ticket.service;

import com.pulse.common.config.JiraProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JiraServiceTest {

    @Mock
    private JiraProperties jiraProperties;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private JiraService jiraService;

    // This should return the ticket data when Jira API responds successfully
    @Test
    void getTicket_returnsTicketData() {
        Map<String, Object> mockResponse = Map.of(
                "key", "SCRUM-5",
                "fields", Map.of("summary", "Test Equity Order cancellation")
        );

        when(jiraProperties.getBaseUrl()).thenReturn("https://test.atlassian.net");
        when(jiraProperties.getEmail()).thenReturn("test@example.com");
        when(jiraProperties.getApiToken()).thenReturn("test_token");
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(requestHeadersSpec).when(requestHeadersSpec).header(anyString(), anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        when(responseSpec.body(Map.class)).thenReturn(mockResponse);

        Map<String, Object> result = jiraService.getTicket("SCRUM-5");

        assertThat(result).containsKey("key");
        assertThat(result.get("key")).isEqualTo("SCRUM-5");
    }

    // This should build a valid Base64 Basic Auth header using the email and token
    @Test
    void buildAuthHeader_encodesCredentialsCorrectly() {
        when(jiraProperties.getEmail()).thenReturn("test@example.com");
        when(jiraProperties.getApiToken()).thenReturn("test_token");
        when(jiraProperties.getBaseUrl()).thenReturn("https://test.atlassian.net");
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(requestHeadersSpec).when(requestHeadersSpec).header(anyString(), anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        when(responseSpec.body(Map.class)).thenReturn(Map.of());

        jiraService.getTicket("SCRUM-5");

        String expectedHeader = "Basic " + java.util.Base64.getEncoder()
                .encodeToString("test@example.com:test_token".getBytes());
        verify(requestHeadersSpec).header("Authorization", expectedHeader);
    }
}