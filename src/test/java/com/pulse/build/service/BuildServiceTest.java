package com.pulse.build.service;

import com.pulse.common.config.GithubProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildServiceTest {

    @Mock
    private GithubProperties githubProperties;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private BuildService buildService;

    // This should trigger the GitHub Actions workflow when valid inputs are provided
    @Test
    void triggerBuild_callsGitHubApiWithCorrectParameters() {
        when(githubProperties.getOwner()).thenReturn("mintthiha");
        when(githubProperties.getRepo()).thenReturn("pulse-demo-tests");
        when(githubProperties.getToken()).thenReturn("gh_token");
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        buildService.triggerBuild("SCRUM-5-branch", "SCRUM-5 - Test scenario");

        verify(restClient).post();
        verify(requestBodySpec).retrieve();
    }
 
    // This should throw an exception when branch is null
    @Test
    void triggerBuild_throwsWhenBranchIsNull() {
        assertThatThrownBy(() -> buildService.triggerBuild(null, "some tests"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Branch must not be null or empty");
    }

    // This should throw an exception when branch is blank
    @Test
    void triggerBuild_throwsWhenBranchIsBlank() {
        assertThatThrownBy(() -> buildService.triggerBuild("  ", "some tests"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Branch must not be null or empty");
    }

    // This should use empty string for tests when null is passed
    @Test
    void triggerBuild_handlesNullTestsGracefully() {
        when(githubProperties.getOwner()).thenReturn("mintthiha");
        when(githubProperties.getRepo()).thenReturn("pulse-demo-tests");
        when(githubProperties.getToken()).thenReturn("gh_token");
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);

        buildService.triggerBuild("SCRUM-5-branch", null);

        verify(restClient).post();
    }
}