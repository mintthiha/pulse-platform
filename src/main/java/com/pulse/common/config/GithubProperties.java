package com.pulse.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "github")
@Getter
@Setter
public class GithubProperties {

    /**
     * GitHub personal access token for API authentication.
     */
    private String token;

    /**
     * GitHub repository owner username.
     */
    private String owner;

    /**
     * GitHub repository name.
     */
    private String repo;
}