package com.pulse.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TestResultRequest {

    @NotBlank(message = "Scenario name must not be blank")
    private String scenarioName;

    @NotBlank(message = "Ticket ID must not be blank")
    private String ticketId;

    @NotBlank(message = "Status must not be blank")
    private String status;

    @NotBlank(message = "Branch must not be blank")
    private String branch;

    @NotNull(message = "Run timestamp must not be null")
    private LocalDateTime runTimestamp;

    private Long durationMs;
    private String[] reportTags;
    private String[] actionTags;
}