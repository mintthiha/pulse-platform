package com.pulse.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jira")
@Getter
@Setter
public class JiraProperties {

    /**
     * Base URL of the Jira Cloud instance e.g. https://yoursite.atlassian.net
     */
    private String baseUrl;

    /**
     * Email address associated with the Jira account.
     */
    private String email;

    /**
     * Jira API token for authentication.
     */
    private String apiToken;
}