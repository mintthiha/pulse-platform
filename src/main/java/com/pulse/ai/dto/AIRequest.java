package com.pulse.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIRequest {

    @NotBlank(message = "Ticket description must not be blank")
    private String ticketDescription;

    private String prDiff;
}