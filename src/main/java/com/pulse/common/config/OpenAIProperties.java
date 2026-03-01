package com.pulse.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openai")
@Getter
@Setter
public class OpenAIProperties {

    /**
     * OpenAI API key for authentication.
     */
    private String apiKey;
}