package com.pulse.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AIResponse {

    private List<String> reportTags;
    private List<String> actionTags;
}