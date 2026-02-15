package com.jkefbq.gymentry.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TicketEvent {
    Ticket ticket;
    TicketDo ticketDo;
}
