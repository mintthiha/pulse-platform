package com.pulse.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TestResultResponse {

    private Long id;
    private String scenarioName;
    private String ticketId;
    private String status;
    private String branch;
    private LocalDateTime runTimestamp;
    private Long durationMs;
    private String[] reportTags;
    private String[] actionTags;
}