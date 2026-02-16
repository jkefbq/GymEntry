package com.jkefbq.gymentry.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TicketEvent {
    UUID eventId;
    Ticket ticket;
    TicketDo ticketDo;

    public TicketEvent(Ticket ticket, TicketDo ticketDo) {
        this.eventId = UUID.randomUUID();
        this.ticket = ticket;
        this.ticketDo = ticketDo;
    }
}
