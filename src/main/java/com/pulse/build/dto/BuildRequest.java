package com.pulse.build.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildRequest {

    @NotBlank(message = "Branch must not be blank")
    private String branch;

    private String tests;
}