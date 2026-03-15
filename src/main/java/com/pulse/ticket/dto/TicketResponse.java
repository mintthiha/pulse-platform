package com.pulse.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketResponse {

    private String ticketId;
    private String summary;
    private String status;
    private Object description;
}