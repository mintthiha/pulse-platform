package com.pulse.pr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PRResponse {

    private Integer number;
    private String title;
    private String branch;
    private String url;
    private String state;
}